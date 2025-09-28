package com.fiap.hospital.bff.core.outputport;


import java.util.Optional;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface UpdateGateway {

    //User    
    Optional<User> update(Long idUser, User user);

    //Type
    Optional<Type> update(Long idType, Type type);

    //Authentication
    void updatePassword(String email, String password);
}
