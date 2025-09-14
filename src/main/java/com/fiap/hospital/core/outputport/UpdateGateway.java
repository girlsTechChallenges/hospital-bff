package com.fiap.hospital.core.outputport;

import com.fiap.hospital.infra.domain.User;

import java.util.Optional;

public interface UpdateGateway {

    Optional<User> update(Long idUser, User user);

    void updatePassword(String email, String password);
}
