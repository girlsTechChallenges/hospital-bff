package com.fiap.hospital.core.usecase;

import com.fiap.hospital.core.outputport.FindByGateway;
import com.fiap.hospital.infra.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetUserUseCases {
    private final FindByGateway findByGateway;


    public GetUserUseCases(FindByGateway findByGateway) {
        this.findByGateway = findByGateway;
    }

    public List<User> getAll() {
        return findByGateway.getAll();
    }

    public Optional<User> getById(Long idUser) {
        return findByGateway.getById(idUser);
    }
}
