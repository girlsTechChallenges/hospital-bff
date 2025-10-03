package com.fiap.hospital.bff.infra.exception;

public class TypeNotFoundException extends BusinessException {

    public TypeNotFoundException(Long typeId) {
        super(String.format("Type %s not found", typeId));
    }

    public TypeNotFoundException(String nameType) {
        super(String.format("Type %s not found", nameType));
    }
}
