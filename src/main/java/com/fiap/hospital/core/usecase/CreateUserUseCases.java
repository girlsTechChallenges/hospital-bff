package com.fiap.hospital.core.usecase;

import com.fiap.hospital.core.outputport.SaveGateway;
import com.fiap.hospital.infra.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCases {

    private final SaveGateway saveGateway;
    private final PasswordEncoder passwordEncoder;


    public CreateUserUseCases(SaveGateway saveGateway, PasswordEncoder passwordEncoder) {
        this.saveGateway = saveGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public User save (User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return saveGateway.save(user);
    }
}
