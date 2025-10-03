package com.fiap.hospital.bff.infra.exception;

public class TypeMismatchException extends BusinessException {

    public TypeMismatchException(String message) {
        super(message);
    }

    public TypeMismatchException(String expectedType, String actualType) {
        super(String.format("User type mismatch. Expected: %s, but got: %s", expectedType, actualType));
    }

}
