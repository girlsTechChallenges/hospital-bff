//package com.fiap.hospital.bff.infra.entrypoint;
//
//import com.fiap.hospital.bff.core.domain.model.user.Type;
//import com.fiap.hospital.bff.core.outputport.DeleteGateway;
//import com.fiap.hospital.bff.core.outputport.GetGateway;
//import com.fiap.hospital.bff.core.outputport.SaveGateway;
//import com.fiap.hospital.bff.core.outputport.UpdateGateway;
//import com.fiap.hospital.bff.infra.entrypoint.controller.TypeController;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
//import com.fiap.hospital.bff.infra.entrypoint.dto.response.TypeResponseDto;
//import com.fiap.hospital.bff.infra.mapper.TypeMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TypeControllerTest {
//
//    @Mock
//    private SaveGateway saveGateway;
//
//    @Mock
//    private GetGateway getGateway;
//
//    @Mock
//    private UpdateGateway updateGateway;
//
//    @Mock
//    private DeleteGateway deleteGateway;
//
//    @Mock
//    private TypeMapper typeMapper;
//
//    @InjectMocks
//    private TypeController typeController;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void create_shouldReturnCreatedType() {
//        TypeRequestDto requestDto = mock(TypeRequestDto.class);
//        Type domainType = mock(Type.class);
//        Type savedType = mock(Type.class);
//        TypeResponseDto responseDto = mock(TypeResponseDto.class);
//
//        when(typeMapper.toTypeEntityDomain(requestDto)).thenReturn(domainType);
//        when(saveGateway.saveType(domainType)).thenReturn(savedType);
//        when(typeMapper.typeEntityResponse(savedType)).thenReturn(responseDto);
//
//        ResponseEntity<TypeResponseDto> response = typeController.create(requestDto);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(responseDto, response.getBody());
//        verify(saveGateway).saveType(domainType);
//    }
//
//    @Test
//    void getAll_shouldReturnListOfTypes() {
//        Type type1 = mock(Type.class);
//        Type type2 = mock(Type.class);
//        List<Type> types = List.of(type1, type2);
//        TypeResponseDto dto1 = mock(TypeResponseDto.class);
//        TypeResponseDto dto2 = mock(TypeResponseDto.class);
//
//        when(getGateway.getAllTypes()).thenReturn(types);
//        when(typeMapper.toTypeResponseDto(type1)).thenReturn(dto1);
//        when(typeMapper.toTypeResponseDto(type2)).thenReturn(dto2);
//
//        ResponseEntity<List<TypeResponseDto>> response = typeController.getAll();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//        assertTrue(response.getBody().contains(dto1));
//        assertTrue(response.getBody().contains(dto2));
//        verify(getGateway).getAllTypes();
//    }
//
//    @Test
//    void update_shouldReturnAcceptedType() {
//        Long id = 1L;
//        TypeRequestDto requestDto = mock(TypeRequestDto.class);
//        Type domainType = mock(Type.class);
//        Optional<Type> updatedType = Optional.of(mock(Type.class));
//        TypeResponseDto responseDto = mock(TypeResponseDto.class);
//
//        when(typeMapper.toTypeEntityDomain(requestDto)).thenReturn(domainType);
//        when(updateGateway.update(id, domainType)).thenReturn(updatedType);
//        when(typeMapper.typeEntityResponse(updatedType)).thenReturn(responseDto);
//
//        ResponseEntity<TypeResponseDto> response = typeController.update(id, requestDto);
//
//        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//        assertEquals(responseDto, response.getBody());
//        verify(updateGateway).update(id, domainType);
//    }
//
//    @Test
//    void getById_shouldReturnTypeIfFound() {
//        Long id = 1L;
//        Type foundType = mock(Type.class);
//        TypeResponseDto responseDto = mock(TypeResponseDto.class);
//
//        when(getGateway.getTypeById(id)).thenReturn(Optional.of(foundType));
//        when(typeMapper.typeEntityResponse(foundType)).thenReturn(responseDto);
//
//        ResponseEntity<TypeResponseDto> response = typeController.getById(id);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDto, response.getBody());
//        verify(getGateway).getTypeById(id);
//    }
//
//    @Test
//    void getById_shouldReturnNotFoundIfMissing() {
//        Long id = 1L;
//
//        when(getGateway.getTypeById(id)).thenReturn(Optional.empty());
//
//        ResponseEntity<TypeResponseDto> response = typeController.getById(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertNull(response.getBody());
//        verify(getGateway).getTypeById(id);
//    }
//}
