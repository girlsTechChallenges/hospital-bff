package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends BusinessException {

    public UserAlreadyRegisteredException(String email) {
        super(String.format("User with email %s is already registered", email), HttpStatus.CONFLICT);
    }
}
