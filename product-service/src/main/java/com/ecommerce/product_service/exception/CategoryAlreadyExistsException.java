package com.ecommerce.product_service.exception;

import jakarta.validation.constraints.NotBlank;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(String name) {
        super(name + " adına ait bir kategori zaten bulunmakta.");
    }
}
