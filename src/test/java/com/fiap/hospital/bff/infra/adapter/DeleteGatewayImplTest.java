//package com.fiap.hospital.bff.infra.adapter;
//
//import com.fiap.hospital.bff.core.domain.model.user.Type;
//import com.fiap.hospital.bff.core.domain.model.user.User;
//import com.fiap.hospital.bff.infra.adapter.gateway.DeleteGatewayImpl;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
//import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
//import com.fiap.hospital.bff.infra.mapper.TypeMapper;
//import com.fiap.hospital.bff.infra.mapper.UserMapper;
//import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
//import com.fiap.hospital.bff.infra.persistence.repository.TypeRepository;
//import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
//import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DeleteGatewayImplTest {
//
//    private UserRepository userRepository;
//    private UserMapper userMapper;
//    private TypeRepository typeRepository;
//    private TypeMapper typeMapper;
//    private DeleteGatewayImpl deleteGateway;
//
//    @BeforeEach
//    void setUp() {
//        userRepository = mock(UserRepository.class);
//        userMapper = mock(UserMapper.class);
//        typeRepository = mock(TypeRepository.class);
//        typeMapper = mock(TypeMapper.class);
//        deleteGateway = new DeleteGatewayImpl(userRepository, userMapper, typeMapper, typeRepository);
//    }
//
//    // -------------------- deleteById --------------------
//
//    @Test
//    void testDeleteUserById_UserExists() {
//        Long userId = 1L;
//        UserEntity userEntity = new UserEntity();
//        User expectedUser = new User();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
//        when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);
//
//        Optional<User> result = deleteGateway.deleteById(userId);
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository).deleteById(userId);
//    }
//
//    @Test
//    void testDeleteUserById_UserNotFound() {
//        Long userId = 99L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> deleteGateway.deleteById(userId));
//
//        verify(userRepository).findById(userId);
//        verify(userRepository, never()).deleteById(anyLong());
//        verify(userMapper, never()).toUserDomain((UserRequestDto) any());
//    }
//
//    // -------------------- deleteTypeById --------------------
//
//    @Test
//    void testDeleteTypeById_TypeExists() {
//        Long typeId = 1L;
//        TypeEntity typeEntity = new TypeEntity();
//        Type expectedType = new Type();
//
//        when(typeRepository.findById(typeId)).thenReturn(Optional.of(typeEntity));
//        when(typeMapper.toTypeDomain(typeEntity)).thenReturn(expectedType);
//
//        Optional<Type> result = deleteGateway.deleteTypeById(typeId);
//
//        assertTrue(result.isPresent());
//        assertEquals(expectedType, result.get());
//
//        verify(typeRepository, times(2)).findById(typeId);
//        verify(typeMapper).toTypeDomain(typeEntity);
//    }
//
//    @Test
//    void testDeleteTypeById_TypeNotFound() {
//        Long typeId = 99L;
//        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> deleteGateway.deleteTypeById(typeId));
//
//        verify(typeRepository).findById(typeId);
//        verify(typeRepository, never()).deleteById(anyLong());
//        verify(typeMapper, never()).toTypeDomain(any());
//    }
//}
