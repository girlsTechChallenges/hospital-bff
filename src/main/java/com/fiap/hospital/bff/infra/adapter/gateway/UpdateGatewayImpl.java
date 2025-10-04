package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;
import java.util.Set;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.exception.TypeAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.exception.TypeMismatchException;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.repository.TypeRepository;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.springframework.stereotype.Service;

@Service
public class UpdateGatewayImpl implements UpdateGateway {

    private static final Logger log = LoggerFactory.getLogger(UpdateGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final UserMapper userMapper;
    private final TypeMapper typeMapper;

    public UpdateGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TypeRepository typeRepository, UserMapper userMapper, TypeMapper typeMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.userMapper = userMapper;
        this.typeMapper = typeMapper;
    }

    @Override
    public Optional<User> update(Long idUser, User user) {
        UserEntity findUser = userRepository.findById(idUser)
                .orElseThrow(() -> {
                    log.warn("User with Id {} not found for update", idUser);
                    return new UserNotFoundException(idUser);
                });

        if (user != null) {
            findUser.setName(user.getName());
            findUser.setEmail(user.getEmail());
            findUser.setPassword(passwordEncoder.encode(user.getPassword()));

            TypeEntity type = findOrCreateType(
                    normalizeTypeName(user.getType().getNameType()),
                    Set.copyOf(user.getType().getRoles()));
            findUser.setType(type);
        }
        log.info("UserEntity {} found for update", findUser);
        UserEntity actualization = userRepository.save(findUser);
        return Optional.ofNullable(userMapper.toDomain(actualization));
    }

    @Override
    public Optional<Type> update(Long idtype, Type type) {
        String normalizedType = normalizeTypeName(type.getNameType());
        TypeEntity existingIdType = typeRepository.findById(idtype)
                .orElseThrow(() -> {
                    log.warn("TypeUser with id {} not found for update", idtype);
                    return new TypeMismatchException("TypeUser not found with id: " + type.getNameType());
                });

        if (!existingIdType.getNameType().equalsIgnoreCase(normalizedType)) {
            typeRepository.findByNameType(normalizedType)
                .ifPresent(existing -> {
                    log.warn("TypeUser with nameType '{}' already exists", normalizedType);
                    throw new TypeAlreadyRegisteredException("TypeUser with nameType '" + normalizedType + "' already exists.");
                });
        }

        existingIdType.setNameType(normalizedType);
        existingIdType.setRoles(Set.copyOf(type.getRoles()));
        TypeEntity updatedEntity = typeRepository.save(existingIdType);
        Type updatedTypeUser = typeMapper.toDomain(updatedEntity);

        log.info("TypeUser updated successfully: {}", updatedTypeUser);
        return Optional.ofNullable(updatedTypeUser);
    }

    private String normalizeTypeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User type cannot be empty.");
        }
        return name.trim().toUpperCase();
    }

    private TypeEntity findOrCreateType(String normalizedType, Set<String> roles) {
        return typeRepository.findByNameType(normalizedType)
                .orElseGet(() -> {
                    TypeEntity newType = TypeEntity.builder()
                        .nameType(normalizedType)
                        .roles(roles)
                        .build();
                    return typeRepository.save(newType);
                });
    }

    @Override
    public void updatePassword(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("Password updated for user with email: {}", email);
    }
}