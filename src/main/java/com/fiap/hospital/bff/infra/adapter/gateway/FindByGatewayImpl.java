package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.FindByGateway;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;

@Component
public class FindByGatewayImpl implements FindByGateway {

    Logger log = LoggerFactory.getLogger(FindByGatewayImpl.class);

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public FindByGatewayImpl(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<User> getAll() {
        log.info("Getting all users");
        return userRepository.findAll().stream().map(mapper::toUserDomain).toList();
    }

    @Override
    public Optional<User> getById(Long idUser) {
        log.info("FindByGatewayImpl.getById: idUser={}", idUser);
        var findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));
        return Optional.ofNullable(mapper.toUserDomain(findUser));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("FindByGatewayImpl.findByEmail: email={}", email);
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.map(mapper::toUserDomain);
    }
}
