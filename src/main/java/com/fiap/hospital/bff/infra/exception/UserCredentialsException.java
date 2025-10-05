package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpStatus;

public class UserCredentialsException extends BusinessException {

    public UserCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UserCredentialsException() {
        super("Invalid credentials provided", HttpStatus.UNAUTHORIZED);
    }
}
