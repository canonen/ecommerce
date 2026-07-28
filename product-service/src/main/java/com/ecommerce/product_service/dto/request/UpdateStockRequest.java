package com.ecommerce.product_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStockRequest {

    @NotNull
    private Integer quantity; // pozitif → stok ekle, negatif → stok düş
}