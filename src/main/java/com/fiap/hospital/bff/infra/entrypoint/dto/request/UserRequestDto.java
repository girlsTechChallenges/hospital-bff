package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Name must contain only letters")
        String name,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "Login is required")
        @Size(min = 5, max = 20)
        String login,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100)
        String password,

        @NotBlank(message = "Type name is required")
        String typeName
) {}