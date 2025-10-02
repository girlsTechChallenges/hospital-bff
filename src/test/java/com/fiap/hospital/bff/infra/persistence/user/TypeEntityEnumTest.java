package com.fiap.hospital.bff.infra.persistence.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeEntityEnumTest {

    @Test
    void testEnumValues() {
        TypeEntityEnum[] expectedValues = {TypeEntityEnum.NURSE, TypeEntityEnum.DOCTOR, TypeEntityEnum.PATIENT};
        
        TypeEntityEnum[] actualValues = TypeEntityEnum.values();
        
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    void testValueOf() {
        assertEquals(TypeEntityEnum.NURSE, TypeEntityEnum.valueOf("NURSE"));
        assertEquals(TypeEntityEnum.DOCTOR, TypeEntityEnum.valueOf("DOCTOR"));
        assertEquals(TypeEntityEnum.PATIENT, TypeEntityEnum.valueOf("PATIENT"));
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            TypeEntityEnum.valueOf("INVALID");
        });
    }
}
