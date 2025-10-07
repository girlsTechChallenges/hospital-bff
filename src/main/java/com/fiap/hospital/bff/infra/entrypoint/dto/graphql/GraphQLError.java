package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

import java.util.List;
import java.util.Map;

public record GraphQLError(
        String message,
        List<String> path,
        Map<String, Object> extensions
) {}
