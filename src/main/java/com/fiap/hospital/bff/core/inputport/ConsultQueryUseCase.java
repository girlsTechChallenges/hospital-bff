package com.fiap.hospital.bff.core.inputport;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface ConsultQueryUseCase {

    //User
    List<User> getAll();
    Optional<User> findByEmail(String email);
    Optional<User> getById(Long idUser);

    //Authentication
    Token validateLogin(String email, String password);

    List<Type> getAllTypes();
    Optional<Type> getTypeById(Long idType);
    Optional<Type> getTypeByName(String nameType);


    
}
