package com.fiap.hospital.infra.config.exception;

public class UserCredentialsException extends RuntimeException {

    public UserCredentialsException(String message) {
        super(message);
    }
}
