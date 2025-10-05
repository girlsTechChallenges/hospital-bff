package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long userId) {
        super(String.format("User with ID %s not found", userId), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super(String.format("User with email %s not found", email), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String message, boolean customMessage) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
