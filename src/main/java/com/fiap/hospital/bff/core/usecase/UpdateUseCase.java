package com.fiap.hospital.bff.core.usecase;

import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.inputport.ConsultCommandUseCase;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;

@Component
public class UpdateUseCase implements ConsultCommandUseCase {

    private final UpdateGateway updateGateway;

    public UpdateUseCase(UpdateGateway updateGateway) {
        this.updateGateway = updateGateway;
    }

    @Override
    public void updatePassword(String email, String password) {
        updateGateway.updatePassword(email, password);
    }
 
}