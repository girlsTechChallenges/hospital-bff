package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.ConsultCommandUseCase;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConsultCommandUseCaseImpl implements ConsultCommandUseCase {

    private final DeleteGateway deleteGateway;
    private final UpdateGateway updateGateway;
    private final SaveGateway saveGateway;
    private final PasswordEncoder passwordEncoder;

    public ConsultCommandUseCaseImpl(DeleteGateway deleteGateway, UpdateGateway updateGateway, SaveGateway saveGateway,
                                     PasswordEncoder passwordEncoder) {
        this.deleteGateway = deleteGateway;
        this.updateGateway = updateGateway;
        this.saveGateway = saveGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveGateway.save(user);
    }

    @Override
    public Optional<User> deleteById(Long idUser) {
        return deleteGateway.deleteById(idUser);
    }

    @Override
    public void updatePassword(String email, String password) {
        updateGateway.updatePassword(email, password);
    }

    @Override
    public Optional<Type> deleteTypeById(Long idType) {
        return deleteGateway.deleteTypeById(idType);
    }
}
