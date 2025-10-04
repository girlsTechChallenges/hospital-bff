package com.fiap.hospital.bff.infra.adapter.gateway;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.mapper.TypeMapper;
import com.fiap.hospital.bff.infra.persistence.repository.TypeRepository;
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
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;

@Component
public class GetGatewayImpl implements GetGateway {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final UserMapper userMapper;
    private final TypeMapper typeMapper;

    public GetGatewayImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder,
                          JwtEncoder jwtEncoder, TypeRepository typeRepository, TypeMapper typeMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Optional<User> getById(Long idUser) {
        var findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        return Optional.ofNullable(userMapper.toDomain(findUser));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDomain);
    }

    @Override
    public Optional<Type> getTypeByName(String name) {
        return typeRepository.findByNameType(name).map(typeMapper::toDomain);
    }

    public Token validateLogin(String email, String password) {
        // Usar query otimizada com FETCH JOIN
        var user = userRepository.findByEmailWithType(email);
        if(user.isEmpty() || !isLoginCorrect(email, password, passwordEncoder)) {
            throw new UserCredentialsException("Invalid email or password");
        }

        var now = Instant.now();
        var expiresIn = 300L;
        // Usar o campo correto 'type' ao invés de 'types'
        var type = user.get().getType().getNameType();
        var scopes = typeRepository.findByNameTypeWithRoles(type)
                .map(t -> t.getRoles().stream().toList())
                .orElse(List.of());

        var claims = JwtClaimsSet.builder()
                .issuer("BackendHospitalBff")
                .subject(email)
                .issuedAt(now)
                .claim("scopes", scopes)
                .expiresAt(now.plusSeconds(expiresIn)).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new Token(jwtValue, expiresIn, scopes);
    }

     private boolean isLoginCorrect(String email, String password, PasswordEncoder passwordEncoder) {
        var userPassword = userRepository.findByEmail(email).stream().map(UserEntity::getPassword).collect(Collectors.joining());
        return passwordEncoder.matches(password, userPassword);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeRepository.findAll().stream().map(typeMapper::toDomain).toList();
    }

    @Override
    public Optional<Type> getTypeById(Long id) {
        return typeRepository.findById(id).map(typeMapper::toDomain);
    }
}
