package com.fiap.hospital.bff.infra.persistence.entity;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    NURSE("Enfermeiro"),
    DOCTOR("Médico"),
    PATIENT("Paciente");

    private final String description;

    UserTypeEnum(String description) {
        this.description = description;
    }
}