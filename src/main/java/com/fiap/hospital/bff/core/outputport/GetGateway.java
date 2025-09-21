package com.fiap.hospital.bff.core.outputport;

import java.util.List;
import java.util.Optional;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface GetGateway {

    //User
    List<User> getAll();
    Optional<User> findByEmail(String email);
    Optional<User> getById(Long idUser);

    //Authentication
    Token validateLogin(String email, String password);
    
}
