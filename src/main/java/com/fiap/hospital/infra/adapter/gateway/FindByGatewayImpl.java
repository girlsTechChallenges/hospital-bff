package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.FindByGateway;
import com.fiap.hospital.infra.domain.User;

import java.util.List;
import java.util.Optional;

public class FindByGatewayImpl implements FindByGateway {
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getById(Long idUser) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
