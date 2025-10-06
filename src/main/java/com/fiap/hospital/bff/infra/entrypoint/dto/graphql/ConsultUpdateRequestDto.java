package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsultUpdateRequestDto(

        @NotNull
        @NotBlank
        @Schema(example = "3", description = "ID da consulta a ser atualizada")
        String id,

        @Schema(example = "Consulta cancelada por conflito de agenda.", description = "Motivo da consulta")
        String reason,

        @Schema(example = "CANCELLED", description = "Status da consulta")
        String status,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2025-10-18", description = "Data da consulta no formato yyyy-MM-dd")
        String date,

        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(example = "17:00:00", description = "Hora da consulta no formato HH:mm:ss")
        String localTime

) {}
