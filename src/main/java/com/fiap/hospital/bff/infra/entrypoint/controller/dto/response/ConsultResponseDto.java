package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

import java.time.LocalDate;

public record ConsultResponseDto(
    String patientName,
    String patientEmail,
    String profissionalName,
    LocalDate consultDateTime,
    String consultReason,
    String consultStatus    
    ) {
} 
