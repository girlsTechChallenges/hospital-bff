package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;

@Component
public class UpdateGatewayImpl implements UpdateGateway {

    @Override
    public Optional<User> update(Long idUser, User user) {
        // TODO: Implementar lógica de atualização de usuário
        return Optional.empty();
    }

    @Override
    public void updatePassword(String email, String password) {
        // TODO: Implementar lógica de atualização de senha
    }
}
