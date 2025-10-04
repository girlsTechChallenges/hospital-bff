package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.TypeRepository;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import org.springframework.stereotype.Service;

@Service
public class DeleteGatewayImpl implements DeleteGateway {

    private static final Logger log = LoggerFactory.getLogger(DeleteGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final TypeEntityMapper typeMapper;
    private final TypeRepository typeRepository;

    public DeleteGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper mapper, TypeEntityMapper typeMapper, TypeRepository typeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.typeMapper = typeMapper;
        this.typeRepository = typeRepository;
    }

    @Override
    public Optional<User> deleteById(Long idUser) {
        UserEntity findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        Optional<UserEntity> user = userRepository.findById(findUser.getId());
        userRepository.deleteById(idUser);
        return user.map(mapper::toUserDomain);
    }

    @Override
    public Optional<Type> deleteTypeById(Long idType) {
        TypeEntity findType = typeRepository.findById(idType)
                .orElseThrow(() -> new UserNotFoundException(idType));

        Optional<TypeEntity> type = typeRepository.findById(idType);
        typeRepository.deleteById(findType.getId());
        return type.map(typeMapper::toTypeDomain);
    }
}
