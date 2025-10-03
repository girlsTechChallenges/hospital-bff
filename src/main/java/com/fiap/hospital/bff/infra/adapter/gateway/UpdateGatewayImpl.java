package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Objects;
import java.util.Optional;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.exception.TypeAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.exception.TypeMismatchException;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UpdateGatewayImpl implements UpdateGateway {

    private static final Logger log = LoggerFactory.getLogger(UpdateGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TypeEntityRepositoryAdapter typeEntityRepositoryAdapter;
    private final UserMapper mapper;
    private final TypeEntityMapper typeMapper;

    public UpdateGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TypeEntityRepositoryAdapter typeEntityRepositoryAdapter, UserMapper mapper, TypeEntityMapper typeMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.typeEntityRepositoryAdapter = typeEntityRepositoryAdapter;
        this.mapper = mapper;
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
            findUser.setPassword(user.getPassword());
            TypeEntity typo = findOrCreateType(
                    normalizeTypeName(user.getType().getNameType()),
                    user.getType().getRoles());
            findUser.setTypes(typo);
        }
        log.info("UserEntity {} found for update", findUser);
        UserEntity actualization = userRepository.save(findUser);
        return Optional.ofNullable(mapper.toUserDomain(actualization));
    }

    @Override
    public Optional<Type>  update(Long IdType, Type type) {
        String normalizedType = normalizeTypeName(type.getNameType());
        TypeEntity existingIdType = typeEntityRepositoryAdapter.findById(IdType)
                .orElseThrow(() -> {
                    log.warn("TypeUser with id {} not found for update", IdType);
                    return new TypeMismatchException("TypeUser not found with id: " + type.getNameType());
                });

        if (!existingIdType.getNameType().equalsIgnoreCase(normalizedType)) {

            typeEntityRepositoryAdapter.findByNameType(normalizedType)
                .ifPresent(existing -> {
                    log.warn("TypeUser with nameType '{}' already exists", normalizedType);
                        throw  new TypeAlreadyRegisteredException("TypeUser with nameType '" + normalizedType + "' already exists.");
                });

        }

        existingIdType.setNameType(normalizedType);
        existingIdType.setRoles(type.getRoles());
        TypeEntity updatedEntity = typeEntityRepositoryAdapter.save(existingIdType);
        Type updatedTypeUser = typeMapper.toTypeEntityDomain(updatedEntity);

        log.info("TypeUser updated successfully: {}", updatedTypeUser);
        return Optional.ofNullable(updatedTypeUser);
    }

    private String normalizeTypeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User type cannot be empty.");
        }
        return name.trim().toUpperCase();
    }

    @Override
    public void updatePassword(String email, String password) {

        var passEncoded = passwordEncoder.encode(password);
        var user = userRepository.findByEmail(email);

        try {
            if(Objects.isNull(user)){
                throw new UserNotFoundException(email);
            }
            user.get().setPassword(passEncoded);
            userRepository.save(user.get());

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(email);

        } catch (Exception e) {
            log.error("Erro ao atualizar a senha do usuário", e);
            throw new RuntimeException("Erro ao atualizar a senha do usuário", e);
        }
    }

    private TypeEntity findOrCreateType(String formattedType, List<String> roles) {
        return typeEntityRepositoryAdapter.findByNameType(formattedType)
                .orElseGet(() -> createNewTypeIfNotExists(formattedType, roles));
    }

    private TypeEntity createNewTypeIfNotExists(String formattedType, List<String> roles) {

        try {
            return typeEntityRepositoryAdapter.save(new TypeEntity(null, formattedType, roles));
        } catch (DataIntegrityViolationException e) {
            return typeEntityRepositoryAdapter.findByNameType(formattedType)
                    .orElseThrow(() -> new IllegalArgumentException("User type already exists."));
        }


    }
}