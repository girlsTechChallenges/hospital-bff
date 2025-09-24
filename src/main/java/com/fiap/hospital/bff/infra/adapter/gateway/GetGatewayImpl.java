package com.fiap.hospital.bff.infra.adapter.gateway;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserRepositoryAdapter;

@Component
public class GetGatewayImpl implements GetGateway {

    Logger log = LoggerFactory.getLogger(GetGatewayImpl.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper mapper;

    public GetGatewayImpl(UserRepositoryAdapter userRepositoryAdapter, UserMapper mapper, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepositoryAdapter = userRepositoryAdapter;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepositoryAdapter.findAll().stream().map(mapper::toUserDomain).toList();
    }

    @Override
    public Optional<User> getById(Long idUser) {
        var findUser = userRepositoryAdapter.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        return Optional.ofNullable(mapper.toUserDomain(findUser));
    }


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = userRepositoryAdapter.findByEmail(email);
        return user.map(mapper::toUserDomain);
    }

    public Token validateLogin(String email, String password) {

        var user = userRepositoryAdapter.findByEmail(email);
        if(user.isEmpty() || !isLoginCorrect(email, password, passwordEncoder)) {
            throw new UserCredentialsException("Invalid email or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;
        var scope = user.map(UserEntity::getTypes);

        var claims = JwtClaimsSet.builder()
                .issuer("BackendHospitalBff")
                .subject(email)
                .issuedAt(now)
                .claim("scope", scope)
                .expiresAt(now.plusSeconds(expiresIn)).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new Token(jwtValue, expiresIn);
    }

     private boolean isLoginCorrect(String email, String password, PasswordEncoder passwordEncoder) {
        var userPassword = userRepositoryAdapter.findByEmail(email).stream().map(UserEntity::getPassword).collect(Collectors.joining());
        return passwordEncoder.matches(password, userPassword);
    }

}
