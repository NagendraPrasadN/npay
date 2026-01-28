package com.gateway.npay.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Data
public class OrderResponse {
    Long id;
    String status;
    Instant createdAt;
    List<OrderItemResponse> items;
}
