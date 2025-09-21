package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UserDto(
        @NotNull
        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "The name must contain only letters")
        String nome,

        @NotNull
        @NotBlank
        @Email
        String email,

        @NotNull
        @NotBlank
        @Size(min = 5, max = 20)
        String login,

        @NotNull
        @NotBlank
        @Size(min = 8, max = 100)
        String senha,

        LocalDate dataAlteracao,

        @NotNull
        @NotBlank
        String tipo
) {}
