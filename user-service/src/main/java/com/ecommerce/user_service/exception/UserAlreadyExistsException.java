package com.ecommerce.user_service.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Bu email zaten kayıtlı: " + email);
    }
}