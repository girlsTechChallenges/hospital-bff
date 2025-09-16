package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.SaveGateway;
import com.fiap.hospital.infra.config.exception.UserAlreadyRegisteredException;
import com.fiap.hospital.infra.domain.User;
import com.fiap.hospital.infra.mapper.UserEntityMapper;
import com.fiap.hospital.infra.persistence.UserEntity;
import com.fiap.hospital.infra.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SaveGatewayImpl implements SaveGateway {

    private static final Logger log = LoggerFactory.getLogger(SaveGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public SaveGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserEntityMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyRegisteredException(
                            "This user already exists. Check your credentials or recover your password."
                    );
                });

        UserEntity userEntity = mapper.toUserEntity(user);
        return mapper.toUserDomain(userRepository.save(userEntity));
    }
}
