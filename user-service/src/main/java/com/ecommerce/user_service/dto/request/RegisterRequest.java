package com.ecommerce.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Size(max = 255, message = "Email 255 karakterden uzun olamaz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, message = "Şifre en az 8 karakter olmalıdır")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "Şifre en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter içermelidir"
    )
    private String password;

    @NotBlank(message = "Ad boş olamaz")
    @Size(min = 2, max = 100, message = "Ad 2-100 karakter arasında olmalıdır")
    private String firstName;

    @NotBlank(message = "Soyad boş olamaz")
    @Size(min = 2, max = 100, message = "Soyad 2-100 karakter arasında olmalıdır")
    private String lastName;
}
