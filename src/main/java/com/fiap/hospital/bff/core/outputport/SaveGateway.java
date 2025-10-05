package com.fiap.hospital.bff.core.outputport;

import com.fiap.hospital.bff.core.domain.model.user.User;

public interface SaveGateway {

    User save(User user);
    
}
