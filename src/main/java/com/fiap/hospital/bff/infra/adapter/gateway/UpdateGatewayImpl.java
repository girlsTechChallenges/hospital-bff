package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateGatewayImpl implements UpdateGateway {

    private static final Logger log = LoggerFactory.getLogger(UpdateGatewayImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdateGatewayImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> update(Long idUser, User user) {
        log.info("Updating user with ID: {}", idUser);

        return userRepository.findById(idUser)
                .map(existingUser -> {
                    Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(user.getLogin()).ifPresent(existingUser::setLogin);
                    Optional.ofNullable(user.getSenha())
                            .filter(senha -> !senha.isEmpty())
                            .map(passwordEncoder::encode)
                            .ifPresent(existingUser::setSenha);

                    return userMapper.toUserDomain(userRepository.save(existingUser));
                });
    }

    @Override
    public void updatePassword(String email, String password) {
        log.info("Updating password for email: {}", email);

        userRepository.findByEmail(email).ifPresent(user -> {
            String encodedPassword = passwordEncoder.encode(password);
            user.setSenha(encodedPassword);
            userRepository.save(user);
        });
    }
}
