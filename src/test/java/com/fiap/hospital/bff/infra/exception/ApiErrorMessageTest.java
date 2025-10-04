//package com.fiap.hospital.bff.infra.exception;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ApiErrorMessageTest {
//
//    @Test
//    void testConstructorWithMap() {
//        Map<String, List<String>> errors = Map.of(
//                "field1", List.of("error1", "error2"),
//                "field2", List.of("error3")
//        );
//        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.BAD_REQUEST, errors);
//
//        assertEquals(HttpStatus.BAD_REQUEST, apiErrorMessage.getStatus());
//        assertEquals(errors, apiErrorMessage.getErrors());
//    }
//
//    @Test
//    void testConstructorWithNullMap() {
//        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, null);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiErrorMessage.getStatus());
//        assertNotNull(apiErrorMessage.getErrors());
//        assertTrue(apiErrorMessage.getErrors().isEmpty());
//    }
//
//    @Test
//    void testConstructorWithFieldAndError() {
//        String field = "username";
//        String error = "must not be empty";
//
//        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, field, error);
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, apiErrorMessage.getStatus());
//        assertNotNull(apiErrorMessage.getErrors());
//        assertTrue(apiErrorMessage.getErrors().containsKey(field));
//        assertEquals(List.of(error), apiErrorMessage.getErrors().get(field));
//    }
//}
