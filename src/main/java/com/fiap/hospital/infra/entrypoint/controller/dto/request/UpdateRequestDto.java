package com.fiap.hospital.infra.entrypoint.controller.dto.request;

import com.fiap.hospital.infra.domain.TypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRequestDto(
        @NotNull(message = "This field cannot be empty") @NotBlank @Size(min = 2, max = 50) String nome,
        @NotNull @NotBlank @Email String email,
        @NotNull @NotBlank @Size(min = 8, max = 100) String senha,
        @NotNull TypeEnum tipo
) {}
