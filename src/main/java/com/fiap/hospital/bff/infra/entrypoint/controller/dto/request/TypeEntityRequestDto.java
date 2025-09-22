package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TypeEntityRequestDto(
        @NotNull
        @NotEmpty
        String type,

        List<String> roles) {
}
