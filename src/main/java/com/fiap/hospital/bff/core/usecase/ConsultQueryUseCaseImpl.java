package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.ConsultQueryUseCase;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsultQueryUseCaseImpl implements ConsultQueryUseCase {

    private final GetGateway getGateway;

    public ConsultQueryUseCaseImpl(GetGateway getGateway) {
        this.getGateway = getGateway;
    }

    /**
     * Implementation of methods related to User
     */

    @Override
    public List<User> getAll() {
        return getGateway.getAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return getGateway.findByEmail(email);
    }

    @Override
    public Optional<User> getById(Long idUser) {
        return getGateway.getById(idUser);
    }

    @Override
    public Token validateLogin(String email, String password) {
        return getGateway.validateLogin(email, password);
    }

    /**
     * Implementation of methods related to Type
     */

    @Override
    public List<Type> getAllTypes() {
        return getGateway.getAllTypes();
    }

    @Override
    public Optional<Type> getTypeById(Long idType) {
        return getGateway.getTypeById(idType);
    }

    @Override
    public Optional<Type> getTypeByName(String nameType) {
        return getGateway.getTypeByName(nameType);
    }
}
