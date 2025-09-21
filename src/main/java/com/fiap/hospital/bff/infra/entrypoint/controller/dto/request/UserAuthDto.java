package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

public record UserAuthDto(String accessToken, Long expiresIn) {
}
