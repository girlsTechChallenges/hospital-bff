package com.fiap.hospital.core.outputport;

import com.fiap.hospital.infra.domain.User;

public interface SaveGateway {
    User save(User user);
}
