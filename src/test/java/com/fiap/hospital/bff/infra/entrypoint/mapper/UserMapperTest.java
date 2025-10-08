package com.fiap.hospital.bff.infra.entrypoint.mapper;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para UserMapper
 * 
 * Testa as conversões entre diferentes representações de dados:
 * - DTO → Domain
 * - Domain → Entity  
 * - Entity → Domain
 * - Domain → Response DTO
 * - Token → Auth DTO
 * 
 * Foca na validação das transformações de dados sem dependências externas
 */
@DisplayName("UserMapper - Testes Unitários")
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Nested
    @DisplayName("Testes de Conversão UserRequestDto para User Domain")
    class ToUserDomainTests {

        @Test
        @DisplayName("Deve converter UserRequestDto para User domain corretamente")
        void shouldConvertUserRequestDtoToUserDomain_Correctly() {
            // Arrange
            UserRequestDto requestDto = TestDataBuilder.createValidUserRequestDto();

            // Act
            User actualUser = userMapper.toUserDomain(requestDto);

            // Assert
            assertThat(actualUser).isNotNull();
            assertThat(actualUser.getNome()).isEqualTo(requestDto.nome());
            assertThat(actualUser.getEmail()).isEqualTo(requestDto.email());
            assertThat(actualUser.getLogin()).isEqualTo(requestDto.login());
            assertThat(actualUser.getSenha()).isEqualTo(requestDto.senha());
            assertThat(actualUser.getTipo()).isEqualTo(requestDto.tipo().name());
        }

        @Test
        @DisplayName("Deve converter diferentes tipos de usuário corretamente")
        void shouldConvertDifferentUserTypes_Correctly() {
            // Arrange & Act & Assert para MEDICO
            UserRequestDto doctorDto = TestDataBuilder.createValidDoctorRequestDto();
            User doctorUser = userMapper.toUserDomain(doctorDto);
            
            assertThat(doctorUser.getTipo()).isEqualTo("MEDICO");
            assertThat(doctorUser.getNome()).contains("Dr.");

            // Arrange & Act & Assert para PACIENTE
            UserRequestDto patientDto = TestDataBuilder.createValidUserRequestDto();
            User patientUser = userMapper.toUserDomain(patientDto);
            
            assertThat(patientUser.getTipo()).isEqualTo("PACIENTE");
        }
    }

    @Nested
    @DisplayName("Testes de Conversão UserUpdateRequestDto para User Domain")
    class ToUserDomainUpdateTests {

        @Test
        @DisplayName("Deve converter UserUpdateRequestDto para User domain com campos nulos apropriados")
        void shouldConvertUserUpdateRequestDtoToUserDomain_WithAppropriateNullFields() {
            // Arrange
            UserUpdateRequestDto updateDto = new UserUpdateRequestDto(
                "novo.email@hospital.com",
                "novoLogin",
                "novaSenha123"
            );

            // Act
            User actualUser = userMapper.toUserDomainUpdate(updateDto);

            // Assert
            assertThat(actualUser).isNotNull();
            assertThat(actualUser.getNome()).isNull(); // Nome não é atualizado
            assertThat(actualUser.getEmail()).isEqualTo(updateDto.email());
            assertThat(actualUser.getLogin()).isEqualTo(updateDto.login());
            assertThat(actualUser.getSenha()).isEqualTo(updateDto.senha());
            assertThat(actualUser.getTipo()).isNull(); // Tipo não é atualizado
        }
    }

    @Nested
    @DisplayName("Testes de Conversão User Domain para UserEntity")
    class ToUserEntityTests {

        @Test
        @DisplayName("Deve converter User domain para UserEntity corretamente")
        void shouldConvertUserDomainToUserEntity_Correctly() {
            // Arrange
            User userDomain = TestDataBuilder.createValidUserDomain();

            // Act
            UserEntity actualEntity = userMapper.toUserEntity(userDomain);

            // Assert
            assertThat(actualEntity).isNotNull();
            assertThat(actualEntity.getNome()).isEqualTo(userDomain.getNome());
            assertThat(actualEntity.getEmail()).isEqualTo(userDomain.getEmail());
            assertThat(actualEntity.getLogin()).isEqualTo(userDomain.getLogin());
            assertThat(actualEntity.getSenha()).isEqualTo(userDomain.getSenha());
            assertThat(actualEntity.getTipo()).isEqualTo(TypeUsers.valueOf(userDomain.getTipo()));
            assertThat(actualEntity.getId()).isNull(); // ID não deve ser definido na criação
        }

        @Test
        @DisplayName("Deve converter diferentes tipos de usuário do domain para entity")
        void shouldConvertDifferentUserTypesFromDomainToEntity() {
            // Arrange & Act & Assert para MEDICO
            User doctorDomain = TestDataBuilder.createValidDoctorDomain();
            UserEntity doctorEntity = userMapper.toUserEntity(doctorDomain);
            
            assertThat(doctorEntity.getTipo()).isEqualTo(TypeUsers.MEDICO);

            // Arrange & Act & Assert para PACIENTE
            User patientDomain = TestDataBuilder.createValidUserDomain();
            UserEntity patientEntity = userMapper.toUserEntity(patientDomain);
            
            assertThat(patientEntity.getTipo()).isEqualTo(TypeUsers.PACIENTE);
        }
    }

    @Nested
    @DisplayName("Testes de Conversão UserEntity para User Domain")
    class EntityToUserDomainTests {

        @Test
        @DisplayName("Deve converter UserEntity para User domain corretamente")
        void shouldConvertUserEntityToUserDomain_Correctly() {
            // Arrange
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();

            // Act
            User actualUser = userMapper.toUserDomain(userEntity);

            // Assert
            assertThat(actualUser).isNotNull();
            assertThat(actualUser.getNome()).isEqualTo(userEntity.getNome());
            assertThat(actualUser.getEmail()).isEqualTo(userEntity.getEmail());
            assertThat(actualUser.getLogin()).isEqualTo(userEntity.getLogin());
            assertThat(actualUser.getSenha()).isEqualTo(userEntity.getSenha());
            assertThat(actualUser.getTipo()).isEqualTo(userEntity.getTipo().name());
        }
    }

    @Nested
    @DisplayName("Testes de Conversão User Domain para UserResponseDto")
    class ToUserResponseDtoTests {

        @Test
        @DisplayName("Deve converter User domain para UserResponseDto corretamente")
        void shouldConvertUserDomainToUserResponseDto_Correctly() {
            // Arrange
            User userDomain = TestDataBuilder.createValidUserDomain();

            // Act
            UserResponseDto actualResponseDto = userMapper.toUserResponseDto(userDomain);

            // Assert
            assertThat(actualResponseDto).isNotNull();
            assertThat(actualResponseDto.id()).isNull(); // ID é sempre null no response (conforme implementação)
            assertThat(actualResponseDto.nome()).isEqualTo(userDomain.getNome());
            assertThat(actualResponseDto.email()).isEqualTo(userDomain.getEmail());
            assertThat(actualResponseDto.login()).isEqualTo(userDomain.getLogin());
            assertThat(actualResponseDto.tipo()).isEqualTo(userDomain.getTipo());
        }

        @Test
        @DisplayName("Deve excluir campos sensíveis no response DTO")
        void shouldExcludeSensitiveFieldsInResponseDto() {
            // Arrange
            User userDomain = TestDataBuilder.createValidUserDomain();
            userDomain.setSenha("senhaSecreta123");

            // Act
            UserResponseDto actualResponseDto = userMapper.toUserResponseDto(userDomain);

            // Assert
            // Verificar que não existe campo senha no response DTO
            assertThat(actualResponseDto.toString()).doesNotContain("senha");
            assertThat(actualResponseDto.toString()).doesNotContain("senhaSecreta123");
        }
    }

    @Nested
    @DisplayName("Testes de Conversão Token para UserAuthRequestDto")
    class ToTokenResponseDtoTests {

        @Test
        @DisplayName("Deve converter Token para UserAuthRequestDto corretamente")
        void shouldConvertTokenToUserAuthRequestDto_Correctly() {
            // Arrange
            String expectedToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZUBlbWFpbC5jb20ifQ.token";
            Long expectedExpiresIn = 300L;
            Token token = new Token(expectedToken, expectedExpiresIn);

            // Act
            UserAuthRequestDto actualAuthDto = userMapper.toTokenResponseDto(token);

            // Assert
            assertThat(actualAuthDto).isNotNull();
            assertThat(actualAuthDto.accessToken()).isEqualTo(expectedToken);
            assertThat(actualAuthDto.expiresIn()).isEqualTo(expectedExpiresIn);
        }

        @Test
        @DisplayName("Deve lidar com token com valores extremos")
        void shouldHandleTokenWithExtremeValues() {
            // Arrange & Act & Assert para token com expiração máxima
            Token longExpirationToken = new Token("token.long.expiration", Long.MAX_VALUE);
            UserAuthRequestDto longExpirationDto = userMapper.toTokenResponseDto(longExpirationToken);
            
            assertThat(longExpirationDto.expiresIn()).isEqualTo(Long.MAX_VALUE);

            // Arrange & Act & Assert para token com expiração mínima
            Token shortExpirationToken = new Token("token.short.expiration", 1L);
            UserAuthRequestDto shortExpirationDto = userMapper.toTokenResponseDto(shortExpirationToken);
            
            assertThat(shortExpirationDto.expiresIn()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("Testes de Conversão Bidirecional")
    class BidirectionalConversionTests {

        @Test
        @DisplayName("Deve manter consistência na conversão Domain → Entity → Domain")
        void shouldMaintainConsistency_InDomainToEntityToDomainConversion() {
            // Arrange
            User originalDomain = TestDataBuilder.createValidUserDomain();

            // Act
            UserEntity entity = userMapper.toUserEntity(originalDomain);
            User convertedBackDomain = userMapper.toUserDomain(entity);

            // Assert
            assertThat(convertedBackDomain.getNome()).isEqualTo(originalDomain.getNome());
            assertThat(convertedBackDomain.getEmail()).isEqualTo(originalDomain.getEmail());
            assertThat(convertedBackDomain.getLogin()).isEqualTo(originalDomain.getLogin());
            assertThat(convertedBackDomain.getSenha()).isEqualTo(originalDomain.getSenha());
            assertThat(convertedBackDomain.getTipo()).isEqualTo(originalDomain.getTipo());
        }

        @Test
        @DisplayName("Deve manter consistência na conversão RequestDto → Domain → ResponseDto")
        void shouldMaintainConsistency_InRequestDtoToDomainToResponseDtoConversion() {
            // Arrange
            UserRequestDto originalRequestDto = TestDataBuilder.createValidUserRequestDto();

            // Act
            User domain = userMapper.toUserDomain(originalRequestDto);
            UserResponseDto responseDto = userMapper.toUserResponseDto(domain);

            // Assert
            assertThat(responseDto.nome()).isEqualTo(originalRequestDto.nome());
            assertThat(responseDto.email()).isEqualTo(originalRequestDto.email());
            assertThat(responseDto.login()).isEqualTo(originalRequestDto.login());
            assertThat(responseDto.tipo()).isEqualTo(originalRequestDto.tipo().name());
        }
    }
}