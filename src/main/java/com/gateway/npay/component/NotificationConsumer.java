package com.gateway.npay.component;

import com.gateway.npay.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    @KafkaListener(topics = "payment-completed", groupId = "notification-service")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        System.out.println("Notify user: Order " + event.orderId() + " status: " + event.status());
    }
}

