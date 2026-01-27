package com.gateway.npay.controller;


import com.gateway.npay.entity.Payment;
import com.gateway.npay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> pay(@RequestParam Long orderId) {
        System.out.println(orderId);
        Payment payment = paymentService.processPayment(orderId);
        return ResponseEntity.ok(payment);
    }

// product id -> order id for each orders -> orderid we should fetch prod details to be ordered the payment
}
