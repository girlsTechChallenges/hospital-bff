package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.TypeRepository;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteGatewayImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private TypeRepository typeRepository;
    private TypeEntityMapper typeMapper;
    private DeleteGatewayImpl deleteGateway;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        typeRepository = mock(TypeRepository.class);
        typeMapper = mock(TypeEntityMapper.class);
        deleteGateway = new DeleteGatewayImpl(userRepository, userMapper, typeMapper, typeRepository);
    }

    // -------------------- deleteById --------------------

    @Test
    void testDeleteUserById_UserExists() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        User expectedUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);

        Optional<User> result = deleteGateway.deleteById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());

        verify(userRepository, times(2)).findById(userId);
        verify(userRepository).deleteById(userId);
        verify(userMapper).toUserDomain(userEntity);
    }

    @Test
    void testDeleteUserById_UserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> deleteGateway.deleteById(userId));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).deleteById(anyLong());
        verify(userMapper, never()).toUserDomain((UserDto) any());
    }

    // -------------------- deleteTypeById --------------------

    @Test
    void testDeleteTypeById_TypeExists() {
        Long typeId = 1L;
        TypeEntity typeEntity = new TypeEntity();
        Type expectedType = new Type();

        when(typeRepository.findById(typeId)).thenReturn(Optional.of(typeEntity));
        when(typeMapper.toTypeDomain(typeEntity)).thenReturn(expectedType);

        Optional<Type> result = deleteGateway.deleteTypeById(typeId);

        assertTrue(result.isPresent());
        assertEquals(expectedType, result.get());

        verify(typeRepository, times(2)).findById(typeId);
        verify(typeRepository).deleteById(typeId);
        verify(typeMapper).toTypeDomain(typeEntity);
    }

    @Test
    void testDeleteTypeById_TypeNotFound() {
        Long typeId = 99L;
        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> deleteGateway.deleteTypeById(typeId));

        verify(typeRepository).findById(typeId);
        verify(typeRepository, never()).deleteById(anyLong());
        verify(typeMapper, never()).toTypeDomain(any());
    }
}
