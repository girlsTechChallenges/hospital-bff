package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.exception.TypeNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.repository.TypeRepository;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DeleteGatewayImpl implements DeleteGateway {

    private static final Logger log = LoggerFactory.getLogger(DeleteGatewayImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TypeMapper typeMapper;
    private final TypeRepository typeRepository;

    public DeleteGatewayImpl(UserRepository userRepository, UserMapper userMapper, TypeMapper typeMapper, TypeRepository typeRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.typeMapper = typeMapper;
        this.typeRepository = typeRepository;
    }

    @Override
    public Optional<User> deleteById(Long idUser) {
        UserEntity findUser = userRepository.findById(idUser)
                .orElseThrow(() -> {
                    log.warn("User with Id {} not found for deletion", idUser);
                    return new UserNotFoundException(idUser);
                });

        User userDomain = userMapper.toDomain(findUser);
        userRepository.deleteById(idUser);
        log.info("User with Id {} deleted successfully", idUser);
        return Optional.of(userDomain);
    }

    @Override
    public Optional<Type> deleteTypeById(Long idType) {
        TypeEntity findType = typeRepository.findById(idType)
                .orElseThrow(() -> {
                    log.warn("Type with Id {} not found for deletion", idType);
                    return new TypeNotFoundException("Type not found with id: " + idType);
                });

        Type typeDomain = typeMapper.toDomain(findType);
        typeRepository.deleteById(idType);
        log.info("Type with Id {} deleted successfully", idType);
        return Optional.of(typeDomain);
    }
}
