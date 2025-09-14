package com.fiap.hospital.infra.entrypoint.controller.dto.response;

import com.fiap.hospital.infra.domain.*;

public record UserResponseDto(
        String nome,
        String login,
        String email,
        TypeEnum tipo) {
}
