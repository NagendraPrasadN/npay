package com.gateway.npay.component;

import com.gateway.npay.dto.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsConsumer {

    @KafkaListener(topics = "order-created", groupId = "analytics-service")
    public void onOrderCreated(OrderPlacedEvent event) {
        System.out.println("Analytics: order " + event.orderId() + " created at " + event.createdAt());
    }
}

