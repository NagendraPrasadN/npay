package com.gateway.npay.component;

import com.gateway.npay.dto.OrderPlacedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderConfirmationListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPlacedEvent event) {
        System.out.println("Order confirmed async: " + event.orderId());
    }
}
