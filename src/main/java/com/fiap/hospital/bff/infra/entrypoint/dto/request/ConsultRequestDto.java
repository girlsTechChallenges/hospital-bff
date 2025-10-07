package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsultRequestDto(

        PatientData patient,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2023-12-31", description = "Data da consulta no formato yyyy-MM-dd")
        String consultDate,

        @NotNull
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(example = "21:00:00", description = "Hora da consulta no formato HH:mm:ss")
        String consultTime,

        @NotBlank
        String consultReason

) {}
