package com.gateway.npay.dto;



public record OrderItemDto(
        Long productId,
        int quantity,
        double price
) {
    public static OrderItemDto from(com.gateway.npay.entity.OrderItem item) {
        return new OrderItemDto(
                item.getProductId(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}

