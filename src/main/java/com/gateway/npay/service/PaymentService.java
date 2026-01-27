package com.gateway.npay.service;

import com.gateway.npay.component.MockPaymentGateway;
import com.gateway.npay.entity.Order;
import com.gateway.npay.entity.Payment;
import com.gateway.npay.enums.OrderStatus;
import com.gateway.npay.enums.PaymentStatus;
import com.gateway.npay.repository.OrderRepository;
import com.gateway.npay.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MockPaymentGateway gateway;

    @Transactional
    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public Payment processPayment(Long orderId) {
        Order order = orderRepository.lockById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Order already paid");
        }

        paymentRepository.findByOrderId(orderId)
                .ifPresent(p -> {
                    throw new RuntimeException("Payment already exists");
                });
        Double amount = calculateOrderAmount(order);
        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
        boolean success = gateway.charge(orderId, amount);


        if (!success) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);

            throw new RuntimeException("Payment failed");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return payment;
    }

    private Double calculateOrderAmount(Order order) {
        return order.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Recover
    public Payment recover(RuntimeException ex, Long orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow();
    }
}
