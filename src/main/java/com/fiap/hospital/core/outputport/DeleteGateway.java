package com.fiap.hospital.core.outputport;

import com.fiap.hospital.infra.domain.User;

import java.util.Optional;

public interface DeleteGateway {
    Optional<User> deleteById(Long idUser);

}
