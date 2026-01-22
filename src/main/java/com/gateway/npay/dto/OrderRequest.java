package com.gateway.npay.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest (
    @NotNull
    Long productId,
    @Min(1) Integer quantity
){}
