package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUseCase {
    private final SaveGateway saveGateway;
    private final PasswordEncoder passwordEncoder;


    public CreateUseCase(SaveGateway saveGateway, PasswordEncoder passwordEncoder) {
        this.saveGateway = saveGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public User save (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveGateway.save(user);
    }
}
