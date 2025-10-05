package com.fiap.hospital.bff.core.usecase;

import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.inputport.AuthenticationCommandUseCase;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;

@Component
public class AuthenticationCommandUseCaseImpl implements AuthenticationCommandUseCase {

    private final UpdateGateway updateGateway;

    public AuthenticationCommandUseCaseImpl(UpdateGateway updateGateway) {
        this.updateGateway = updateGateway;
    }

    @Override
    public void updatePassword(String email, String password) {
        updateGateway.updatePassword(email, password);
    }
}
