package com.fiap.hospital.infra.config.exception;

public class MenuAlreadyRegisteredException extends RuntimeException {
    public MenuAlreadyRegisteredException(String message) {
        super(message);
    }
}
