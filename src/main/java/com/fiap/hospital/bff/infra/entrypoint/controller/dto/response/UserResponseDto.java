package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;

public record UserResponseDto(
        String name,
        String login,
        String email,
        String type) {
}
