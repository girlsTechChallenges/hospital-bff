package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.DeleteGateway;
import com.fiap.hospital.infra.domain.User;

import java.util.Optional;

public class DeleteGatewayImpl implements DeleteGateway {


    @Override
    public Optional<User> deleteById(Long idUser) {
        return Optional.empty();
    }
}
