package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for external service failures
 */
public class ExternalServiceException extends BusinessException {

    public ExternalServiceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }

}
