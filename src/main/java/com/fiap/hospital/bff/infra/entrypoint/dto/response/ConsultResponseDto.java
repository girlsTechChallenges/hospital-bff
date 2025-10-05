package com.fiap.hospital.bff.infra.entrypoint.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ConsultResponseDto(
    Long id,
    Long patientId,
    Long doctorId,
    String patientName,
    String patientEmail,
    String profissionalName,
    LocalDate consultDateTime,
    String consultReason,
    String consultStatus    
) {
}
