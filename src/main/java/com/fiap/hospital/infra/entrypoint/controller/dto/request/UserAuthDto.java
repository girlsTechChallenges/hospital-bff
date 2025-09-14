package com.fiap.hospital.infra.entrypoint.controller.dto.request;

public record UserAuthDto(String accessToken, Long expiresIn) {
}
