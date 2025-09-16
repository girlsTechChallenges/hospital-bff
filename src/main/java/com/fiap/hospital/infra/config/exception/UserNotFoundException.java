package com.fiap.hospital.infra.config.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super(String.format("User %s not found", userId));
    }

    public UserNotFoundException(String email) {
        super(String.format("User %s not found", email));
    }
}
