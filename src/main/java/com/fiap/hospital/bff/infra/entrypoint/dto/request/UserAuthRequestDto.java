package com.fiap.hospital.bff.infra.entrypoint.dto.request;

public record UserAuthRequestDto(String accessToken, Long expiresIn) {
}
