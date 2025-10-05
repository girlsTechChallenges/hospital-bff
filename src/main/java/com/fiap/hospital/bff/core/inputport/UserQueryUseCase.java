package com.fiap.hospital.bff.core.inputport;

import java.util.List;
import java.util.Optional;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface UserQueryUseCase {
    
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
}
