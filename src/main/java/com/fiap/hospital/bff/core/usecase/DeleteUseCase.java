package com.fiap.hospital.bff.core.usecase;


import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeleteUseCase {
    private final DeleteGateway deleteGateway;

    public DeleteUseCase(DeleteGateway deleteGateway) {
        this.deleteGateway = deleteGateway;
    }

    public Optional<User> delete(Long idUser) {
        return deleteGateway.deleteById(idUser);
    }
}
