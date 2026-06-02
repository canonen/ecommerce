package com.ecommerce.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token boş olamaz")
    private String refreshToken;
}
