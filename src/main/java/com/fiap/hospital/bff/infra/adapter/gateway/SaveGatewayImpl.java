package com.fiap.hospital.bff.infra.adapter.gateway;

import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.SaveGateway;

@Component
public class SaveGatewayImpl implements SaveGateway {

    @Override
    public User save(User user) {
        // TODO: Implementar lógica de salvamento de usuário
        return user;
    }
}
