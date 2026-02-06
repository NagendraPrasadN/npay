package com.gateway.npay.dto;



import java.time.Instant;
import java.util.List;

public record OrderPlacedEvent (Long orderId,
                               Instant createdAt,
                               List<OrderItemDto> items){}