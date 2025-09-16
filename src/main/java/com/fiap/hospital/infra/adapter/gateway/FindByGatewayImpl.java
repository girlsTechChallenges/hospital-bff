package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.FindByGateway;
import com.fiap.hospital.infra.config.exception.UserNotFoundException;
import com.fiap.hospital.infra.domain.User;
import com.fiap.hospital.infra.mapper.UserEntityMapper;
import com.fiap.hospital.infra.persistence.UserEntity;
import com.fiap.hospital.infra.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindByGatewayImpl implements FindByGateway {

    private static final Logger log = LoggerFactory.getLogger(FindByGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public FindByGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserEntityMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(mapper::toUserDomain);
    }

    @Override
    public Optional<User> getById(Long idUser) {
        var findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        return Optional.ofNullable(mapper.toUserDomain(findUser));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll().stream().map(mapper::toUserDomain).toList();
    }
}
