package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntityRepositoryAdapter;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateGatewayImplTest {

    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TypeEntityRepositoryAdapter typeEntityRepositoryAdapter;
    private UserMapper userMapper;
    private TypeEntityMapper typeMapper;

    private UpdateGatewayImpl updateGateway;

    @BeforeEach
    void setup() {
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        typeEntityRepositoryAdapter = mock(TypeEntityRepositoryAdapter.class);
        userMapper = mock(UserMapper.class);
        typeMapper = mock(TypeEntityMapper.class);

        updateGateway = new UpdateGatewayImpl(passwordEncoder, userRepository, typeEntityRepositoryAdapter, userMapper, typeMapper);
    }

    @Test
    void updateUser_WhenUserNotFound_ThrowsException() {
        Long idUser = 1L;
        User user = new User();

        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateGateway.update(idUser, user));
    }

    @Test
    void updateUser_SuccessfulUpdate_ReturnsUpdatedUser() {
        Long idUser = 1L;
        User user = new User();
        user.setName("Updated Name");
        user.setEmail("updated@example.com");
        user.setPassword("newpass");
        user.setType(new Type(1L,"ADMIN", List.of("ROLE_ADMIN")));

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setName("Old Name");
        existingUserEntity.setEmail("old@example.com");
        existingUserEntity.setPassword("oldpass");
        existingUserEntity.setTypes(new TypeEntity(1L, "USER", List.of("ROLE_USER")));

        TypeEntity newTypeEntity = new TypeEntity(2L, "ADMIN", List.of("ROLE_ADMIN"));
        UserEntity updatedUserEntity = new UserEntity();

        when(userRepository.findById(idUser)).thenReturn(Optional.of(existingUserEntity));
        when(typeEntityRepositoryAdapter.findByNameType("ADMIN")).thenReturn(Optional.of(newTypeEntity));
        when(userRepository.save(existingUserEntity)).thenReturn(updatedUserEntity);
        when(userMapper.toUserDomain(updatedUserEntity)).thenReturn(user);

        Optional<User> result = updateGateway.update(idUser, user);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        // Verifica se o usuário foi atualizado corretamente
        assertEquals("Updated Name", existingUserEntity.getName());
        assertEquals("updated@example.com", existingUserEntity.getEmail());
        assertEquals("newpass", existingUserEntity.getPassword());
        assertEquals(newTypeEntity, existingUserEntity.getTypes());
    }

    @Test
    void updateType_WhenTypeNotFound_ThrowsException() {
        Long idType = 1L;
        Type type = new Type(1L,"ADMIN", List.of("ROLE_ADMIN"));

        when(typeEntityRepositoryAdapter.findById(idType)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateGateway.update(idType, type));
    }

    @Test
    void updateType_WhenNameTypeAlreadyExists_ThrowsException() {
        Long idType = 1L;
        Type type = new Type(1L,"ADMIN", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        TypeEntity existingTypeEntity = new TypeEntity(idType, "USER", List.of("ROLE_USER"));
        TypeEntity conflictingTypeEntity = new TypeEntity(2L, normalizedName, List.of("ROLE_ADMIN"));

        when(typeEntityRepositoryAdapter.findById(idType)).thenReturn(Optional.of(existingTypeEntity));
        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.of(conflictingTypeEntity));

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> updateGateway.update(idType, type));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void updateType_SuccessfulUpdate_ReturnsUpdatedType() {
        Long idType = 1L;
        Type type = new Type(1L,"ADMIN", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        TypeEntity existingTypeEntity = new TypeEntity(idType, "USER", List.of("ROLE_USER"));
        TypeEntity updatedTypeEntity = new TypeEntity(idType, normalizedName, type.getRoles());

        when(typeEntityRepositoryAdapter.findById(idType)).thenReturn(Optional.of(existingTypeEntity));
        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.empty());
        when(typeEntityRepositoryAdapter.save(existingTypeEntity)).thenReturn(updatedTypeEntity);
        when(typeMapper.toTypeEntityDomain(updatedTypeEntity)).thenReturn(type);

        Optional<Type> result = updateGateway.update(idType, type);

        assertTrue(result.isPresent());
        assertEquals(type, result.get());
    }

    @Test
    void updatePassword_SuccessfulUpdate() {
        String email = "user@example.com";
        String password = "newpassword";
        String encodedPassword = "encodedpassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        assertDoesNotThrow(() -> updateGateway.updatePassword(email, password));
        assertEquals(encodedPassword, userEntity.getPassword());
        verify(userRepository).save(userEntity);
    }

}
