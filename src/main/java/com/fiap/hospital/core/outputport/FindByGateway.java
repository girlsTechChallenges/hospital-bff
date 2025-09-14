package com.fiap.hospital.core.outputport;

import com.fiap.hospital.infra.domain.User;

import java.util.List;
import java.util.Optional;

public interface FindByGateway {

    Optional<User> findByEmail(String email);

    Optional<User> getById(Long idUser);

    List<User> getAll();
}
