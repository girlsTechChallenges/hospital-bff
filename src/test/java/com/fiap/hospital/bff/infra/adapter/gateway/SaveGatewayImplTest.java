package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.exception.UserAlreadyRegisteredException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntityRepositoryAdapter;
import com.fiap.hospital.bff.infra.persistence.user.TypeRepository;
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

class SaveGatewayImplTest {

    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private TypeRepository typeRepository;
    private UserMapper userMapper;
    private TypeEntityMapper typeMapper;
    private TypeEntityRepositoryAdapter typeEntityRepositoryAdapter;

    private SaveGatewayImpl saveGateway;

    @BeforeEach
    void setup() {
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        typeRepository = mock(TypeRepository.class);
        userMapper = mock(UserMapper.class);
        typeMapper = mock(TypeEntityMapper.class);
        typeEntityRepositoryAdapter = mock(TypeEntityRepositoryAdapter.class);

        saveGateway = new SaveGatewayImpl(passwordEncoder, userRepository, typeRepository, userMapper, typeMapper, typeEntityRepositoryAdapter);
    }

    @Test
    void saveUser_WhenEmailExists_ThrowsException() {
        User user = new User();
        user.setEmail("email@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(UserAlreadyRegisteredException.class, () -> saveGateway.save(user));
    }

    @Test
    void saveUser_SuccessfulSave_ReturnsUser() {
        User user = new User();
        user.setEmail("email@example.com");
        user.setType(new Type(1L,"DOCTOR", List.of("ROLE_DOCTOR")));

        UserEntity userEntity = new UserEntity();
        UserEntity savedUserEntity = new UserEntity();

        TypeEntity typeEntity = new TypeEntity();
        typeEntity.setNameType("DOCTOR");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUserEntity(user)).thenReturn(userEntity);
        when(typeRepository.findByNameType("DOCTOR")).thenReturn(Optional.of(typeEntity));
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toUserDomain(savedUserEntity)).thenReturn(user);

        User result = saveGateway.save(user);

        verify(userRepository).save(userEntity);
        assertEquals(user, result);
    }

    @Test
    void saveType_WhenNameIsBlank_ThrowsIllegalArgumentException() {
        Type type = new Type(1L,"   ", List.of("ROLE_USER"));

        assertThrows(IllegalArgumentException.class, () -> saveGateway.saveType(type));
    }

    @Test
    void saveType_WhenTypeExists_ReturnsExistingType() {
        Type type = new Type(1L,"ADMIN", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        TypeEntity existingTypeEntity = new TypeEntity();
        existingTypeEntity.setNameType(normalizedName);

        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.of(existingTypeEntity));
        when(typeMapper.toTypeEntityDomain(existingTypeEntity)).thenReturn(type);

        Type result = saveGateway.saveType(type);

        assertEquals(type, result);
        verify(typeEntityRepositoryAdapter, never()).save(any());
    }

    @Test
    void saveType_WhenTypeDoesNotExist_SavesAndReturnsNewType() {
        Type type = new Type(1L,"admin", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        TypeEntity newTypeEntity = new TypeEntity(null, normalizedName, type.getRoles());
        TypeEntity savedTypeEntity = new TypeEntity(1L, normalizedName, type.getRoles());

        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.empty());
        when(typeEntityRepositoryAdapter.save(any(TypeEntity.class))).thenReturn(savedTypeEntity);
        when(typeMapper.toTypeEntityDomain(savedTypeEntity)).thenReturn(type);

        Type result = saveGateway.saveType(type);

        assertEquals(type, result);

        // Capturing argument to verify correct normalization and roles set
        ArgumentCaptor<TypeEntity> captor = ArgumentCaptor.forClass(TypeEntity.class);
        verify(typeEntityRepositoryAdapter).save(captor.capture());

        TypeEntity captured = captor.getValue();
        assertEquals(normalizedName, captured.getNameType());
        assertEquals(type.getRoles(), captured.getRoles());
    }

    @Test
    void saveType_WhenSaveThrowsDataIntegrityViolationException_FindsExistingType() {
        Type type = new Type(1L,"ADMIN", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        TypeEntity newTypeEntity = new TypeEntity(null, normalizedName, type.getRoles());
        TypeEntity existingTypeEntity = new TypeEntity(1L, normalizedName, type.getRoles());

        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.empty());
        when(typeEntityRepositoryAdapter.save(any(TypeEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate key"));

        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.of(existingTypeEntity));
        when(typeMapper.toTypeEntityDomain(existingTypeEntity)).thenReturn(type);

        Type result = saveGateway.saveType(type);

        assertEquals(type, result);
        verify(typeEntityRepositoryAdapter, times(1)).findByNameType(normalizedName); // one before save, one after exception
    }

    @Test
    void saveType_WhenSaveThrowsDataIntegrityViolationExceptionAndTypeNotFound_ThrowsIllegalArgumentException() {
        Type type = new Type(1L,"admin", List.of("ROLE_ADMIN"));
        String normalizedName = "ADMIN";

        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.empty());
        when(typeEntityRepositoryAdapter.save(any(TypeEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate key"));
        when(typeEntityRepositoryAdapter.findByNameType(normalizedName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> saveGateway.saveType(type));
    }
}
