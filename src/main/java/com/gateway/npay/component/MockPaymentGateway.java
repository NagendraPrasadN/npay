package com.gateway.npay.component;

import org.springframework.stereotype.Component;

@Component
public class MockPaymentGateway {

    public boolean charge(Long orderId, Double amount) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}

        return Math.random() > 0.1; // 90% success
    }
}