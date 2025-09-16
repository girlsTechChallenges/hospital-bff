package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.DeleteGateway;
import com.fiap.hospital.infra.config.exception.UserNotFoundException;
import com.fiap.hospital.infra.domain.User;
import com.fiap.hospital.infra.mapper.UserEntityMapper;
import com.fiap.hospital.infra.persistence.UserEntity;
import com.fiap.hospital.infra.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteGatewayImpl implements DeleteGateway {
    private static final Logger log = LoggerFactory.getLogger(DeleteGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public DeleteGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserEntityMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> deleteById(Long idUser) {
        UserEntity findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        Optional<UserEntity> user = userRepository.findById(idUser);
        userRepository.deleteById(idUser);
        return user.map(mapper::toUserDomain);
    }
}
