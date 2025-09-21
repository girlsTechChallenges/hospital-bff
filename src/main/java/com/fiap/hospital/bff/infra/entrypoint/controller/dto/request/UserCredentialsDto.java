package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserCredentialsDto(
        @NotNull String email,
        @NotNull String password) {
}
