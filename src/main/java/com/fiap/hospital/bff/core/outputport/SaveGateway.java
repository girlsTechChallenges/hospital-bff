package com.fiap.hospital.bff.core.outputport;


import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;

public interface SaveGateway {

    //User    
    User save(User user);

    Type saveType(Type type);
}
