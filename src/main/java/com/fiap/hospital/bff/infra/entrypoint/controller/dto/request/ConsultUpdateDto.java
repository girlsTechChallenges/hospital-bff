package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ConsultUpdateDto(
       
        @NotNull
        @NotBlank
        String consultAnalysis,

        @NotNull
        @NotBlank
        String profissionalName,

        @NotNull
        @NotBlank
        LocalDate consultDateTime,

        @NotNull
        @NotBlank
        String consultStatus
) {}
