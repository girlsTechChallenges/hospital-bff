package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.exception.TypeNotFoundException;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntityRepositoryAdapter;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetGatewayImplTest {

    private UserRepositoryAdapter userRepositoryAdapter;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private TypeEntityRepositoryAdapter typeEntityRepositoryAdapter;
    private TypeEntityMapper typeMapper;

    private GetGatewayImpl getGateway;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = mock(UserRepositoryAdapter.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);
        typeEntityRepositoryAdapter = mock(TypeEntityRepositoryAdapter.class);
        typeMapper = mock(TypeEntityMapper.class);

        getGateway = new GetGatewayImpl(
                userRepositoryAdapter,
                userMapper,
                passwordEncoder,
                jwtEncoder,
                typeEntityRepositoryAdapter,
                typeMapper);
    }

    @Test
    void testGetAllReturnsMappedUsers() {
        UserEntity userEntity = mock(UserEntity.class);
        User userDomain = new User();
        when(userRepositoryAdapter.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toUserDomain(userEntity)).thenReturn(userDomain);

        List<User> users = getGateway.getAll();

        assertEquals(1, users.size());
        assertSame(userDomain, users.get(0));
        verify(userRepositoryAdapter).findAll();
        verify(userMapper).toUserDomain(userEntity);
    }

    @Test
    void testGetById_UserExists_ReturnsUser() {
        Long userId = 1L;
        UserEntity userEntity = mock(UserEntity.class);
        User userDomain = new User();

        when(userRepositoryAdapter.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDomain(userEntity)).thenReturn(userDomain);

        Optional<User> result = getGateway.getById(userId);

        assertTrue(result.isPresent());
        assertSame(userDomain, result.get());
    }

    @Test
    void testGetById_UserNotFound_ThrowsException() {
        Long userId = 1L;
        when(userRepositoryAdapter.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getGateway.getById(userId));
    }

    @Test
    void testFindByEmail_UserExists_ReturnsUser() {
        String email = "test@example.com";
        UserEntity userEntity = mock(UserEntity.class);
        User userDomain = new User();

        when(userRepositoryAdapter.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDomain(userEntity)).thenReturn(userDomain);

        Optional<User> result = getGateway.findByEmail(email);

        assertTrue(result.isPresent());
        assertSame(userDomain, result.get());
    }

    @Test
    void testFindByEmail_UserNotFound_ReturnsEmpty() {
        String email = "notfound@example.com";
        when(userRepositoryAdapter.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = getGateway.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void testValidateLogin_Success_ReturnsToken() {
        String email = "test@example.com";
        String rawPassword = "rawPass";
        String encodedPassword = "encodedPass";

        UserEntity userEntity = mock(UserEntity.class);
        TypeEntity typeEntity = mock(TypeEntity.class);
        when(userRepositoryAdapter.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userEntity.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(userEntity.getTypes()).thenReturn(typeEntity);

        var fakeTokenValue = "fake.jwt.token";
        var jwt = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwt.getTokenValue()).thenReturn(fakeTokenValue);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        Token token = getGateway.validateLogin(email, rawPassword);

        assertNotNull(token);
        assertEquals(fakeTokenValue, token.getAccessToken());
        assertEquals(300L, token.getExpiresIn());

        // Verify the encoding was called
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testValidateLogin_InvalidEmail_ThrowsException() {
        when(userRepositoryAdapter.findByEmail("bademail")).thenReturn(Optional.empty());

        assertThrows(UserCredentialsException.class,
                () -> getGateway.validateLogin("bademail", "anyPassword"));
    }

    @Test
    void testValidateLogin_InvalidPassword_ThrowsException() {
        String email = "test@example.com";
        String rawPassword = "rawPass";
        String encodedPassword = "encodedPass";

        UserEntity userEntity = mock(UserEntity.class);
        when(userRepositoryAdapter.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userEntity.getPassword()).thenReturn(encodedPassword);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertThrows(UserCredentialsException.class,
                () -> getGateway.validateLogin(email, rawPassword));
    }

    @Test
    void testGetAllTypes_ReturnsMappedTypes() {
        TypeEntity typeEntity = mock(TypeEntity.class);
        Type typeDomain = new Type();

        when(typeEntityRepositoryAdapter.findAll()).thenReturn(List.of(typeEntity));
        when(typeMapper.toTypeEntityDomain(typeEntity)).thenReturn(typeDomain);

        List<Type> types = getGateway.getAllTypes();

        assertEquals(1, types.size());
        assertSame(typeDomain, types.get(0));
    }

    @Test
    void testGetTypeById_TypeExists_ReturnsType() {
        Long typeId = 1L;
        TypeEntity typeEntity = mock(TypeEntity.class);
        Type typeDomain = new Type();

        when(typeEntityRepositoryAdapter.findById(typeId)).thenReturn(Optional.of(typeEntity));
        when(typeMapper.toTypeEntityDomain(typeEntity)).thenReturn(typeDomain);

        Optional<Type> result = getGateway.getTypeById(typeId);

        assertTrue(result.isPresent());
        assertSame(typeDomain, result.get());
    }

    @Test
    void testGetTypeById_TypeNotFound_ThrowsException() {
        Long typeId = 99L;
        when(typeEntityRepositoryAdapter.findById(typeId)).thenReturn(Optional.empty());

        assertThrows(TypeNotFoundException.class, () -> getGateway.getTypeById(typeId));
    }
}
