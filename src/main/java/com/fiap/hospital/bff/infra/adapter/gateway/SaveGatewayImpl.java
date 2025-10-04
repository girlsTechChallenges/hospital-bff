package com.fiap.hospital.bff.infra.adapter.gateway;


import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.infra.exception.UserAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveGatewayImpl implements SaveGateway {

    private static final Logger log = LoggerFactory.getLogger(SaveGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final UserMapper userMapper;
    private final TypeEntityMapper typeMapper;
    private final TypeEntityRepositoryAdapter typeEntityRepositoryAdapter;

    public SaveGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TypeRepository typeRepository, UserMapper userMapper, TypeEntityMapper typeMapper, TypeEntityRepositoryAdapter typeUserRepositoryAdapter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.userMapper = userMapper;
        this.typeMapper = typeMapper;
        this.typeEntityRepositoryAdapter = typeUserRepositoryAdapter;
    }

    @Override
    public User save(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyRegisteredException(
                            "This user already exists. Check your credentials or recover your password."
                    );
                });

        UserEntity userEntity = userMapper.toUserEntity(user);

        TypeEntity type = typeRepository.findByNameType(user.getType().getNameType())
                .orElseThrow(() -> new RuntimeException("Type not found"));

        userEntity.setTypes(type);

        UserEntity savedUser = userRepository.save(userEntity);

        return userMapper.toUserDomain(savedUser);
    }


    @Override
    public Type saveType(Type type) {
        String normalizedType = normalizeTypeName(type.getNameType());
        TypeEntity typeEntity = findOrCreateType(normalizedType, type.getRoles());
        return typeMapper.toTypeEntityDomain(typeEntity);

    }

    private String normalizeTypeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User type cannot be empty.");
        }
        return name.trim().toUpperCase();
    }

    private TypeEntity findOrCreateType(String normalizedType, List<String> roles) {
        return typeEntityRepositoryAdapter.findByNameType(normalizedType)
                .orElseGet(() -> safelySaveType(normalizedType, roles));
    }

    private TypeEntity safelySaveType(String normalizedType, List<String> roles) {
        try {
            return typeEntityRepositoryAdapter.save(new TypeEntity(null, normalizedType, roles));
        } catch (DataIntegrityViolationException e) {
            return typeEntityRepositoryAdapter.findByNameType(normalizedType)
                    .orElseThrow(() -> new IllegalArgumentException("User type already exists."));
        }
    }
}
