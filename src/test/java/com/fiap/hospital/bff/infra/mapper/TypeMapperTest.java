//package com.fiap.hospital.bff.infra.mapper;
//
//import com.fiap.hospital.bff.core.domain.model.user.Type;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
//import com.fiap.hospital.bff.infra.entrypoint.dto.response.TypeResponseDto;
//import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static com.fiap.hospital.bff.infra.common.MessageConstants.USER_NOT_FOUND;
//import static org.junit.jupiter.api.Assertions.*;
//
//class TypeMapperTest {
//
//    private TypeMapper mapper;
//
//    @BeforeEach
//    void setUp() {
//        mapper = new TypeMapper();
//    }
//
//    @Test
//    void toTypeEntityDomain_FromRequestDto_ShouldMapCorrectly() {
//        TypeRequestDto dto = new TypeRequestDto("Admin", List.of("ROLE_ADMIN", "ROLE_USER"));
//        Type type = mapper.toTypeEntityDomain(dto);
//
//        assertNull(type.getId());
//        assertEquals("Admin", type.getNameType());
//        assertEquals(List.of("ROLE_ADMIN", "ROLE_USER"), type.getRoles());
//    }
//
//    @Test
//    void toTypeDomain_FromTypeEntity_ShouldMapCorrectly() {
//        TypeEntity entity = new TypeEntity(1L, "User", List.of("ROLE_USER"));
//        Type type = mapper.toTypeDomain(entity);
//
//        assertNull(type.getId());  // note que seu método sempre seta null no id
//        assertEquals("User", type.getNameType());
//        assertEquals(List.of("ROLE_USER"), type.getRoles());
//    }
//
//    @Test
//    void toTypeEntity_FromType_ShouldMapCorrectly() {
//        Type type = new Type(2L, "Manager", List.of("ROLE_MANAGER"));
//        TypeEntity entity = mapper.toTypeEntity(type);
//
//        assertNull(entity.getId());  // seu método seta null no id
//        assertEquals("Manager", entity.getNameType());
//        assertEquals(List.of("ROLE_MANAGER"), entity.getRoles());
//    }
//
//    @Test
//    void toTypeEntityDomain_FromTypeEntity_ShouldMapCorrectlyWithId() {
//        TypeEntity entity = new TypeEntity(10L, "Support", List.of("ROLE_SUPPORT"));
//        Type type = mapper.toTypeEntityDomain(entity);
//
//        assertEquals(10L, type.getId());
//        assertEquals("Support", type.getNameType());
//        assertEquals(List.of("ROLE_SUPPORT"), type.getRoles());
//    }
//
//    @Test
//    void typeEntityResponse_FromType_ShouldMapCorrectly() {
//        Type type = new Type(3L, "Guest", List.of("ROLE_GUEST"));
//        TypeResponseDto response = mapper.typeEntityResponse(type);
//
//        assertEquals(3L, response.id());
//        assertEquals("Guest", response.nameType());
//        assertEquals(List.of("ROLE_GUEST"), response.roles());
//    }
//
//    @Test
//    void typeEntityResponse_FromOptionalPresent_ShouldReturnResponse() {
//        Type type = new Type(4L, "Editor", List.of("ROLE_EDITOR"));
//        Optional<Type> optional = Optional.of(type);
//
//        TypeResponseDto response = mapper.typeEntityResponse(optional);
//
//        assertEquals(4L, response.id());
//        assertEquals("Editor", response.nameType());
//        assertEquals(List.of("ROLE_EDITOR"), response.roles());
//    }
//
//    @Test
//    void typeEntityResponse_FromOptionalEmpty_ShouldThrowException() {
//        Optional<Type> optional = Optional.empty();
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            mapper.typeEntityResponse(optional);
//        });
//        assertEquals("TypeUser not found", exception.getMessage());
//    }
//
//    @Test
//    void getTypeByIdToTypeResponseDto_Present_ShouldReturnResponse() {
//        Type type = new Type(5L, "Admin", List.of("ROLE_ADMIN"));
//        Optional<Type> optional = Optional.of(type);
//
//        TypeResponseDto response = mapper.getTypeByIdToTypeResponseDto(optional);
//
//        assertEquals(5L, response.id());
//        assertEquals("Admin", response.nameType());
//        assertEquals(List.of("ROLE_ADMIN"), response.roles());
//    }
//
//    @Test
//    void getTypeByIdToTypeResponseDto_Empty_ShouldThrowRuntimeException() {
//        Optional<Type> optional = Optional.empty();
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            mapper.getTypeByIdToTypeResponseDto(optional);
//        });
//        assertEquals(USER_NOT_FOUND, exception.getMessage());
//    }
//
//    @Test
//    void toTypeResponseDto_ShouldMapCorrectly() {
//        Type type = new Type(6L, "Viewer", List.of("ROLE_VIEWER"));
//        TypeResponseDto response = mapper.toTypeResponseDto(type);
//
//        assertEquals(6L, response.id());
//        assertEquals("Viewer", response.nameType());
//        assertEquals(List.of("ROLE_VIEWER"), response.roles());
//    }
//}
