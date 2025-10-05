package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.UserQueryUseCase;
import com.fiap.hospital.bff.core.outputport.FindByGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserQueryUseCaseImpl implements UserQueryUseCase {

    private final FindByGateway findByGateway;

    public UserQueryUseCaseImpl(FindByGateway findByGateway) {
        this.findByGateway = findByGateway;
    }

    @Override
    public List<User> getAllUsers() {
        return findByGateway.getAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return findByGateway.getById(id);
    }
}
