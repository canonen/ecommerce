package com.ecommerce.product_service.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ProductResponse {
    private CategoryResponse categoryResponse;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
