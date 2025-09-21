package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;

@Component
public class DeleteGatewayImpl implements DeleteGateway {

    @Override
    public Optional<User> deleteById(Long idUser) {
        // TODO: Implementar lógica de exclusão de usuário
        return Optional.empty();
    }
}
