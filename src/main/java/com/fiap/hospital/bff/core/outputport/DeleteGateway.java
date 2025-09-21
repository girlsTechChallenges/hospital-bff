package com.fiap.hospital.bff.core.outputport;


import java.util.Optional;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface DeleteGateway {

    //User
       Optional<User> deleteById(Long idUser);
   
}
