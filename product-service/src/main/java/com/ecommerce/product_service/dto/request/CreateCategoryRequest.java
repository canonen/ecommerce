package com.ecommerce.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CreateCategoryRequest {

    @NotBlank(message = "name alanı boş olamaz.")
    private String name;

    private String description;
}
