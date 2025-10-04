package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserCredentialsRequestDto(
        @NotNull String email,
        @NotNull String password) {
}
