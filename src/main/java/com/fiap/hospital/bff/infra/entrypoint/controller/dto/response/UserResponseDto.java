package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

public record UserResponseDto(
        String name,
        String login,
        String email,
        String type) {
}
