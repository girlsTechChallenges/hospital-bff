package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.infra.exception.UserAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.mapper.TypeMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.TypeRepository;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SaveGatewayImpl implements SaveGateway {

    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final UserMapper userMapper;
    private final TypeMapper typeMapper;

    public SaveGatewayImpl(UserRepository userRepository,
                           TypeRepository typeRepository,
                           UserMapper userMapper,
                           TypeMapper typeMapper) {
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.userMapper = userMapper;
        this.typeMapper = typeMapper;
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyRegisteredException(
                    "This user already exists. Check your credentials or recover your password."
            );
        }

        // Primeiro resolve o tipo
        String normalizedType = normalizeTypeName(user.getType().getNameType());
        TypeEntity typeEntity = typeRepository.findByNameType(normalizedType)
                .orElseThrow(() -> new RuntimeException("Type not found"));

        // Agora mapeia o usuário com o tipo resolvido
        UserEntity userEntity = userMapper.toEntity(user, typeEntity);

        UserEntity savedUser = userRepository.save(userEntity);

        return userMapper.toDomain(savedUser);
    }

    @Override
    public Type saveType(Type type) {
        String normalizedType = normalizeTypeName(type.getNameType());

        // Garante que o tipo seja criado com o enum correto
        TypeEntity typeEntity = typeMapper.toEntity(new Type(
                type.getId(),
                normalizedType,
                type.getRoles()
        ));

        try {
            return typeMapper.toDomain(typeRepository.save(typeEntity));
        } catch (DataIntegrityViolationException e) {
            return typeRepository.findByNameType(normalizedType)
                    .map(typeMapper::toDomain)
                    .orElseThrow(() -> new IllegalArgumentException("User type already exists."));
        }
    }

    private String normalizeTypeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User type cannot be empty.");
        }
        return name.trim().toUpperCase();
    }
}