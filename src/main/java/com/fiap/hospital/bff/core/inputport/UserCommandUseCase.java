package com.fiap.hospital.bff.core.inputport;

import java.util.Optional;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface UserCommandUseCase {
    
    User createUser(User user);
    Optional<User> updateUser(Long id, User user);
    Optional<User> deleteUser(Long id);
}
