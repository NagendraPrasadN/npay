package com.gateway.npay.dto;

import com.gateway.npay.enums.PaymentStatus;

public record PaymentCompletedEvent(
        Long orderId,
        Double amount,
        PaymentStatus status
) {}