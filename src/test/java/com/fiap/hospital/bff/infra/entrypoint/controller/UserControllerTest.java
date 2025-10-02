package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private SaveGateway saveGateway;

    @Mock
    private GetGateway getGateway;

    @Mock
    private UpdateGateway updateGateway;

    @Mock
    private DeleteGateway deleteGateway;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnCreatedUser() {
        UserDto userDto = mock(UserDto.class);
        User domainUser = mock(User.class);
        User savedUser = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userMapper.toUserDomain(userDto)).thenReturn(domainUser);
        when(saveGateway.save(domainUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDto(savedUser)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.create(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(saveGateway).save(domainUser);
    }

    @Test
    void getAll_shouldReturnListOfUsers() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        List<User> users = List.of(user1, user2);
        UserResponseDto dto1 = mock(UserResponseDto.class);
        UserResponseDto dto2 = mock(UserResponseDto.class);

        when(getGateway.getAll()).thenReturn(users);
        when(userMapper.toUserResponseDto(user1)).thenReturn(dto1);
        when(userMapper.toUserResponseDto(user2)).thenReturn(dto2);

        ResponseEntity<List<UserResponseDto>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(dto1));
        assertTrue(response.getBody().contains(dto2));
        verify(getGateway).getAll();
    }

    @Test
    void getById_shouldReturnUserResponse() {
        Long id = 1L;
        User user = mock(User.class);
        Optional<User> optionalUser = Optional.of(user);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(getGateway.getById(id)).thenReturn(optionalUser);
        when(userMapper.getUserByIdToUserResponseDto(optionalUser)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(getGateway).getById(id);
    }

    @Test
    void update_shouldReturnUpdatedUserResponse() {
        Long id = 1L;
        UpdateRequestDto updateRequestDto = mock(UpdateRequestDto.class);
        User updatedDomainUser = mock(User.class);
        User updatedUser = mock(User.class);
        UserResponseDto responseDto = mock(UserResponseDto.class);

        when(userMapper.updateUserDomain(updateRequestDto)).thenReturn(updatedDomainUser);
        when(updateGateway.update(id, updatedDomainUser)).thenReturn(Optional.ofNullable(updatedUser));
        when(userMapper.getUserByIdToUserResponseDto(Optional.ofNullable(updatedUser))).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.update(id, updateRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(updateGateway).update(id, updatedDomainUser);
    }

}
