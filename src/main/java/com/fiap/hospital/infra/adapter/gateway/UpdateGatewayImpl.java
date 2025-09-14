package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.UpdateGateway;
import com.fiap.hospital.infra.domain.User;

import java.util.Optional;

public class UpdateGatewayImpl implements UpdateGateway {
    @Override
    public Optional<User> update(Long idUser, User user) {
        return Optional.empty();
    }

    @Override
    public void updatePassword(String email, String password) {

    }
}
