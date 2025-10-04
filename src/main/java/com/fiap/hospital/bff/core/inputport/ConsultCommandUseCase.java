package com.fiap.hospital.bff.core.inputport;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;

import java.util.Optional;

public interface ConsultCommandUseCase {
    
    void updatePassword(String email, String password);

    User save(User user);

    Optional<User> deleteById(Long idUser);

    Optional<Type> deleteTypeById(Long idType);
    
}
