package com.fiap.hospital.bff.infra.entrypoint.dto.response;

import java.util.List;

public record TypeResponseDto(
        Long id,
        String nameType,
        List<String> roles)  {
}

