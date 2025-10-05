package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ConsultRequestDto(
        @NotNull
        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "The name must contain only letters")
        String patientName,

        @NotNull
        @NotBlank
        @Email
        String patientEmail,

        @NotNull
        @NotBlank
        @Email
        String profissionalEmail,

        @NotNull
        @NotBlank
        LocalDate consultDateTime,

        @NotNull
        @NotBlank
        String consultReason
) {}
