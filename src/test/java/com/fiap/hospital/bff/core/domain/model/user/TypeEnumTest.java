//package com.fiap.hospital.bff.core.domain.model.user;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TypeEnumTest {
//
//    @Test
//    void testEnumValues() {
//        TypeEnum[] values = TypeEnum.values();
//        assertEquals(3, values.length);
//        assertArrayEquals(new TypeEnum[]{TypeEnum.NURSE, TypeEnum.DOCTOR, TypeEnum.PATIENT}, values);
//    }
//
//    @Test
//    void testValueOf() {
//        assertEquals(TypeEnum.NURSE, TypeEnum.valueOf("NURSE"));
//        assertEquals(TypeEnum.DOCTOR, TypeEnum.valueOf("DOCTOR"));
//        assertEquals(TypeEnum.PATIENT, TypeEnum.valueOf("PATIENT"));
//    }
//
//    @Test
//    void testInvalidValueOfThrowsException() {
//        assertThrows(IllegalArgumentException.class, () -> TypeEnum.valueOf("ADMIN"));
//    }
//}
