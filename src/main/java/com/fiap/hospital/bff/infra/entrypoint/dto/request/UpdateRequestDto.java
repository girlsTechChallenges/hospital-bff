package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record  UpdateRequestDto(
        @NotNull(message = "This field cannot be empty") @NotBlank
        @Size(min = 2, max = 50) String name,
        @NotNull @NotBlank @Email
        String email,
        @NotNull @NotBlank @Size(min = 8, max = 100) String password,
        @NotNull Type type
) {}

