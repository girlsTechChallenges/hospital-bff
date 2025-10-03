package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import java.util.List;

public record UserAuthDto(String accessToken, Long expiresIn, List<String> scopes) {
}
