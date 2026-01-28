package com.gateway.npay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.npay.dto.OrderPlacedEvent;
import com.gateway.npay.dto.OrderRequest;
import com.gateway.npay.entity.Order;
import com.gateway.npay.entity.OrderItem;
import com.gateway.npay.entity.Product;
import com.gateway.npay.enums.OrderItemResponse;
import com.gateway.npay.enums.OrderResponse;
import com.gateway.npay.enums.OrderStatus;
import com.gateway.npay.repository.OrderRepository;
import com.gateway.npay.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    @CacheEvict(value = "order", key = "#order.productId")
    public OrderResponse placeOrder(@Valid OrderRequest request) {
      Product product = productRepository.findById(request.productId()).orElseThrow(() -> new RuntimeException("Product not found"));
      if(product.getStockQuantity() < request.quantity()){
          throw new RuntimeException("Insufficient stock");
      }
        product.setStockQuantity(product.getStockQuantity() - request.quantity());

        Order order = Order.builder().build();

        OrderItem item = OrderItem.builder()
                .productId(product.getId())
                .quantity(request.quantity())
                .price(product.getPrice())
                .order(order)
                .build();
        order.setItems(List.of(item));
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(
                new OrderPlacedEvent(saved.getId())
        );
        return map(saved);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "order", key = "#id")
    public OrderResponse getOrder(Long id) {
        System.out.println("Fetching from DB...");
        return   orderRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        }

@Transactional
@CacheEvict(value = "order", key = "#id")
    public void cancelOrder(Long id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));


    if (order.getStatus() == OrderStatus.CANCELLED) return;


    for (OrderItem item : order.getItems()) {
        Product product = productRepository
                .findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

        product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
    }

    order.setStatus(OrderStatus.CANCELLED);

    }

    private OrderResponse map(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getProductId(),
                                i.getQuantity()
                        )).toList()
        );
    }
}
