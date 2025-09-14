package com.fiap.hospital.core.usecase;

import com.fiap.hospital.core.outputport.UpdateGateway;
import com.fiap.hospital.infra.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UpdateUserUseCases {
    private final UpdateGateway updateGateway;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdateUserUseCases(UpdateGateway updateGateway, BCryptPasswordEncoder passwordEncoder) {
        this.updateGateway = updateGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> update(Long idUser, User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return updateGateway.update(idUser, user);
    }
}
