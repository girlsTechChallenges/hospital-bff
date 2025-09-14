package com.fiap.hospital.core.usecase;

import com.fiap.hospital.core.outputport.DeleteGateway;
import com.fiap.hospital.infra.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeleteUserUseCases {

    private final DeleteGateway deleteGateway;

    public DeleteUserUseCases(DeleteGateway deleteGateway) {
        this.deleteGateway = deleteGateway;
    }

    public Optional<User> delete(Long idUser) {
        return deleteGateway.deleteById(idUser);
    }
}
