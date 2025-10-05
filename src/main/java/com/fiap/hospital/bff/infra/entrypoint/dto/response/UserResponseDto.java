package com.fiap.hospital.bff.infra.entrypoint.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(
    Long id,
    String nome,
    String email,
    String login,
    String tipo
) {}
