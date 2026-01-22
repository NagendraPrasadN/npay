package com.gateway.npay.enums;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        Instant createdAt,
        List<OrderItemResponse> items
) {}
