package com.fiap.hospital.infra.adapter.gateway;

import com.fiap.hospital.core.outputport.UpdateGateway;
import com.fiap.hospital.infra.config.exception.UserNotFoundException;
import com.fiap.hospital.infra.domain.User;
import com.fiap.hospital.infra.mapper.UserEntityMapper;
import com.fiap.hospital.infra.persistence.TypeEntityEnum;
import com.fiap.hospital.infra.persistence.UserEntity;
import com.fiap.hospital.infra.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UpdateGatewayImpl implements UpdateGateway {

    private static final Logger log = LoggerFactory.getLogger(UpdateGatewayImpl.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public UpdateGatewayImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserEntityMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> update(Long idUser, User user) {
        UserEntity findUser = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        if (user != null) {
            findUser.setNome(user.getNome());
            findUser.setEmail(user.getEmail());
            findUser.setSenha(user.getSenha());
            findUser.setTipo(TypeEntityEnum.valueOf(user.getTipo().name()));

        }

        UserEntity actualization = userRepository.save(findUser);
        return Optional.ofNullable(mapper.toUserDomain(actualization));
    }

    @Override
    public void updatePassword(String email, String password) {

        var passEncoded = passwordEncoder.encode(password);
        var user = userRepository.findByEmail(email);

        try {
            if(Objects.isNull(user)){
                throw new UserNotFoundException(email);
            }
            user.get().setSenha(passEncoded);
            userRepository.save(user.get());

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(email);

        } catch (Exception e) {
            log.error("Erro ao atualizar a senha do usuário", e);
            throw new RuntimeException("Erro ao atualizar a senha do usuário", e);
        }
    }
}
