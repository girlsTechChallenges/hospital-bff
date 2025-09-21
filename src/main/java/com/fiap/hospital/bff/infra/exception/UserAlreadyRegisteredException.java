package com.fiap.hospital.bff.infra.exception;

public class UserAlreadyRegisteredException extends BusinessException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

}
