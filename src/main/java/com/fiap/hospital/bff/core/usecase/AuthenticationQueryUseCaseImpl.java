package com.fiap.hospital.bff.core.usecase;

import java.time.Instant;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.AuthenticationQueryUseCase;
import com.fiap.hospital.bff.core.outputport.FindByGateway;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;

@Component
public class AuthenticationQueryUseCaseImpl implements AuthenticationQueryUseCase {

    private final FindByGateway findByGateway;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthenticationQueryUseCaseImpl(FindByGateway findByGateway, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.findByGateway = findByGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public Token validateLogin(String email, String password) {
        var user = findByGateway.findByEmail(email);

        if (user.isEmpty() || !isPasswordValid(email, password)) {
            throw new UserCredentialsException("Invalid email or password");
        }

        return generateToken(email, user.get());
    }

    private boolean isPasswordValid(String email, String password) {
        var userPassword = findByGateway.findByEmail(email)
            .map(User::getSenha)
            .orElse("");
        return passwordEncoder.matches(password, userPassword);
    }

    private Token generateToken(String email, User user) {
        var now = Instant.now();
        var expiresIn = 300L;
        var scope = user.getTipo();

        var claims = JwtClaimsSet.builder()
                .issuer("HospitalAPI")
                .subject(email)
                .issuedAt(now)
                .claim("scope", scope)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new Token(jwtValue, expiresIn);
    }
}
