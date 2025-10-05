package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.exception.UserAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SaveGatewayImpl implements SaveGateway {

    private static final Logger log = LoggerFactory.getLogger(SaveGatewayImpl.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public SaveGatewayImpl(UserRepository userRepository, UserMapper mapper,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {

        log.info("Creating new user with email: {}", user.getEmail());

        var existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyRegisteredException(user.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encodedPassword);

        var entity = mapper.toUserEntity(user);
        var savedEntity = userRepository.save(entity);
        return mapper.toUserDomain(savedEntity);
    }
}
