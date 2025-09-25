package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

import java.util.List;

public record TypeEntityResponse(String nameType, List<String> roles)  {
}

