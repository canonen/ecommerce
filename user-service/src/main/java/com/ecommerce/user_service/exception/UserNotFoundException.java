package com.ecommerce.user_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Kullanıcı bulunamadı: " + id);
    }
}
