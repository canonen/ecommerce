package com.ecommerce.product_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank
    private long categoryId;

    @NotBlank
    private String name;

    private String description;

    @PositiveOrZero
    private double price;

    @PositiveOrZero
    private int stock;

    @NotBlank
    private String imageUrl;

    private boolean isActive;
}
