package com.webmotech.spring_security_jwt.util.mapper.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
