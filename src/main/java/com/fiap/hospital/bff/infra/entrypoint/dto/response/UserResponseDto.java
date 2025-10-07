package com.fiap.hospital.bff.infra.entrypoint.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(
    Long id,
    String nome,
    String email,
    String login,
    TypeUsers tipo
) {}
