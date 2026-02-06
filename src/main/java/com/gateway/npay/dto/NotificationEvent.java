package com.gateway.npay.dto;

public record NotificationEvent(
        String type,
        String payload
) {}
