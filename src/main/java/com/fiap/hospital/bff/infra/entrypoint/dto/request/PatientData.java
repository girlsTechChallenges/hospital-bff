package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import jakarta.validation.constraints.*;

public record PatientData(
        @NotNull @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "The name must contain only letters")
        String name,

        @NotNull @NotBlank @Email
        String email){
}
