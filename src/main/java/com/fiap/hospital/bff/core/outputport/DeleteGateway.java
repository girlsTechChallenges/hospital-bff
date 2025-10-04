package com.fiap.hospital.bff.core.outputport;


import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;

import java.util.Optional;

public interface DeleteGateway {

       Optional<User> deleteById(Long idUser);

       Optional<Type> deleteTypeById(Long idType);
}
