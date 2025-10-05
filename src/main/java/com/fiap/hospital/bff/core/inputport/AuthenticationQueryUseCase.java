package com.fiap.hospital.bff.core.inputport;

import com.fiap.hospital.bff.core.domain.model.token.Token;

public interface AuthenticationQueryUseCase {

    Token validateLogin(String email, String password);

}
