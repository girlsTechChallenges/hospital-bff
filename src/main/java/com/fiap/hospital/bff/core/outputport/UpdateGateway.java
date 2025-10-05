package com.fiap.hospital.bff.core.outputport;

import java.util.Optional;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface UpdateGateway {

    Optional<User> update(Long idUser, User user);
    void updatePassword(String email, String password);
    
}
