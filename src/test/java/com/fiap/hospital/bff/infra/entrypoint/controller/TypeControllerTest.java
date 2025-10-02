package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.TypeEntityResponse;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeControllerTest {

    @Mock
    private SaveGateway saveGateway;

    @Mock
    private GetGateway getGateway;

    @Mock
    private UpdateGateway updateGateway;

    @Mock
    private DeleteGateway deleteGateway;

    @Mock
    private TypeEntityMapper typeEntityMapper;

    @InjectMocks
    private TypeController typeController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnCreatedType() {
        TypeEntityRequestDto requestDto = mock(TypeEntityRequestDto.class);
        Type domainType = mock(Type.class);
        Type savedType = mock(Type.class);
        TypeEntityResponse responseDto = mock(TypeEntityResponse.class);

        when(typeEntityMapper.toTypeEntityDomain(requestDto)).thenReturn(domainType);
        when(saveGateway.saveType(domainType)).thenReturn(savedType);
        when(typeEntityMapper.typeEntityResponse(savedType)).thenReturn(responseDto);

        ResponseEntity<TypeEntityResponse> response = typeController.create(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(saveGateway).saveType(domainType);
    }

    @Test
    void getAll_shouldReturnListOfTypes() {
        Type type1 = mock(Type.class);
        Type type2 = mock(Type.class);
        List<Type> types = List.of(type1, type2);
        TypeEntityResponse dto1 = mock(TypeEntityResponse.class);
        TypeEntityResponse dto2 = mock(TypeEntityResponse.class);

        when(getGateway.getAllTypes()).thenReturn(types);
        when(typeEntityMapper.toTypeResponseDto(type1)).thenReturn(dto1);
        when(typeEntityMapper.toTypeResponseDto(type2)).thenReturn(dto2);

        ResponseEntity<List<TypeEntityResponse>> response = typeController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(dto1));
        assertTrue(response.getBody().contains(dto2));
        verify(getGateway).getAllTypes();
    }

    @Test
    void update_shouldReturnAcceptedType() {
        Long id = 1L;
        TypeEntityRequestDto requestDto = mock(TypeEntityRequestDto.class);
        Type domainType = mock(Type.class);
        Optional<Type> updatedType = Optional.of(mock(Type.class));
        TypeEntityResponse responseDto = mock(TypeEntityResponse.class);

        when(typeEntityMapper.toTypeEntityDomain(requestDto)).thenReturn(domainType);
        when(updateGateway.update(id, domainType)).thenReturn(updatedType);
        when(typeEntityMapper.typeEntityResponse(updatedType)).thenReturn(responseDto);

        ResponseEntity<TypeEntityResponse> response = typeController.update(id, requestDto);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(updateGateway).update(id, domainType);
    }

    @Test
    void getById_shouldReturnTypeIfFound() {
        Long id = 1L;
        Type foundType = mock(Type.class);
        TypeEntityResponse responseDto = mock(TypeEntityResponse.class);

        when(getGateway.getTypeById(id)).thenReturn(Optional.of(foundType));
        when(typeEntityMapper.typeEntityResponse(foundType)).thenReturn(responseDto);

        ResponseEntity<TypeEntityResponse> response = typeController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(getGateway).getTypeById(id);
    }

    @Test
    void getById_shouldReturnNotFoundIfMissing() {
        Long id = 1L;

        when(getGateway.getTypeById(id)).thenReturn(Optional.empty());

        ResponseEntity<TypeEntityResponse> response = typeController.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(getGateway).getTypeById(id);
    }
}
