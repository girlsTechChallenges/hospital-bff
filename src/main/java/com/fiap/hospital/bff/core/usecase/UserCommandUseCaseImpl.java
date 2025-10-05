package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.UserCommandUseCase;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCommandUseCaseImpl implements UserCommandUseCase {

    private final SaveGateway saveGateway;
    private final UpdateGateway updateGateway;
    private final DeleteGateway deleteGateway;

    public UserCommandUseCaseImpl(SaveGateway saveGateway,
                                  UpdateGateway updateGateway,
                                  DeleteGateway deleteGateway) {
        this.saveGateway = saveGateway;
        this.updateGateway = updateGateway;
        this.deleteGateway = deleteGateway;
    }

    @Override
    public User createUser(User user) {
        return saveGateway.save(user);
    }

    @Override
    public Optional<User> updateUser(Long id, User user) {
        return updateGateway.update(id, user);
    }

    @Override
    public Optional<User> deleteUser(Long id) {
        return deleteGateway.deleteById(id);
    }
}
