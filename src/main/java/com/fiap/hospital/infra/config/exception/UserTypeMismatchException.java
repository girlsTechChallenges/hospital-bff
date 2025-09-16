package com.fiap.hospital.infra.config.exception;

public class UserTypeMismatchException extends RuntimeException {
    public UserTypeMismatchException(String message) {
        super(message);
    }
}
