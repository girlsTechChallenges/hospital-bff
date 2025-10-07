package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsultDeleteRequestDto(

        @NotNull
        @NotBlank
        @Schema(example = "2", description = "ID da consulta a ser deletada")
        String id

) {}
