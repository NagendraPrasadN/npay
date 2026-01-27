package com.gateway.npay.enums;

public enum OrderStatus {
    CREATED,        // order placed but not paid
    PAID,           // payment successful
    PAYMENT_FAILED, // payment attempted but failed
    CANCELLED       // order cancelled
}
