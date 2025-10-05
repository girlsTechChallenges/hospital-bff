package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for business validation errors
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ValidationException(String field, String error) {
        super(String.format("Validation error in field '%s': %s", field, error), HttpStatus.BAD_REQUEST);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST);
    }
}
