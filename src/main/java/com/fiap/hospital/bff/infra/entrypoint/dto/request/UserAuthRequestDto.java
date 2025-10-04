package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import java.util.List;

public record UserAuthRequestDto(String accessToken, Long expiresIn, List<String> scopes) {
}
