package com.fiap.hospital.infra.entrypoint.controller.dto.request;

import com.fiap.hospital.infra.domain.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


@NotNull
@NotBlank
public record UserRequestDto(

        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "The name must contain only letters")
        String nome,

        @Email
        String email,

        @Size(min = 5, max = 20)
        String login,

        @Size(min = 8, max = 100) String senha,
        LocalDate dataAlteracao,

        TypeEnum tipo
) {}

