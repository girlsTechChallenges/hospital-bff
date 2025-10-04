package com.fiap.hospital.bff.core.usecase;

import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.inputport.ConsultQueryUseCase;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;

@Component
public class GetUseCase implements ConsultQueryUseCase {

    private final GetGateway getGateway;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public GetUseCase(GetGateway getGateway, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.getGateway = getGateway;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public Token validateLogin(String email, String password) {

        var user = getGateway.findByEmail(email);
        if(user.isEmpty() || !isLoginCorrect(email, password, passwordEncoder)) {
            throw new UserCredentialsException("Invalid email or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;
        var type = user.get().getType().getNameType();
        var scopes = getGateway.getTypeByName(type).stream()
                .flatMap(t -> t.getRoles().stream())
                .toList();
        var claims = JwtClaimsSet.builder()
                .issuer("BackendHospitalBFF")
                .subject(email)
                .issuedAt(now)
                .claim("scopes", scopes)
                .expiresAt(now.plusSeconds(expiresIn)).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new Token(jwtValue, expiresIn, scopes);
    }

    private boolean isLoginCorrect(String email, String password, PasswordEncoder passwordEncoder) {
        var userPassword = getGateway.findByEmail(email).stream().map(User::getPassword).collect(Collectors.joining());
        return passwordEncoder.matches(password, userPassword);
    }

}