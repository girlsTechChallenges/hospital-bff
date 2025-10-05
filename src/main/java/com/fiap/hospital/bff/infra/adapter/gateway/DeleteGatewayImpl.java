package com.fiap.hospital.bff.infra.adapter.gateway;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;

@Component
public class DeleteGatewayImpl implements DeleteGateway {

    private static final Logger log = LoggerFactory.getLogger(DeleteGatewayImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DeleteGatewayImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> deleteById(Long idUser) {
        log.info("Deleting user with ID: {}", idUser);

        return userRepository.findById(idUser)
                .map(userEntity -> {
                    User userDomain = userMapper.toUserDomain(userEntity);
                    userRepository.deleteById(idUser);
                    return userDomain;
                });
    }
}
