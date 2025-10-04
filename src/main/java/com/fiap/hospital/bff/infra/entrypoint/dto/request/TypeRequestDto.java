package com.fiap.hospital.bff.infra.entrypoint.dto.request;

import com.fiap.hospital.bff.infra.persistence.entity.UserTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TypeRequestDto(
        @NotBlank(message = "Type name is required")
        @Size(min = 2, max = 50)
        String nameType,

        @NotNull(message = "User type is required")
        UserTypeEnum userType,

        @NotEmpty(message = "At least one role must be provided")
        List<@NotBlank String> roles
) {}
