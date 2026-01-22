package com.gateway.npay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="catalog")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Min(value = 0, message = "Price must be non-negative")
    @Column(nullable = false)
    private double price;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    @Column(nullable = false)
    private int stockQuantity;

    @Version
    @Column(nullable = false)
    private Long version;

}
