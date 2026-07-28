package com.ecommerce.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    private String imageUrl;

    private Boolean active;
}