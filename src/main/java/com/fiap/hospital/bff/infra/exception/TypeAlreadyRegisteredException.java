package com.fiap.hospital.bff.infra.exception;

public class TypeAlreadyRegisteredException extends BusinessException {

    public TypeAlreadyRegisteredException(String message) {
        super(message);
    }

}
