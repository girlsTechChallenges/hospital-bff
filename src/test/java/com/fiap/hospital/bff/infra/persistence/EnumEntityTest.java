//package com.fiap.hospital.bff.infra.persistence;
//
//import com.fiap.hospital.bff.infra.persistence.entity.UserTypeEnum;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserTypeEnumTest {
//
//    @Test
//    void testEnumValues() {
//        UserTypeEnum[] expectedValues = {UserTypeEnum.NURSE, UserTypeEnum.DOCTOR, UserTypeEnum.PATIENT};
//
//        UserTypeEnum[] actualValues = UserTypeEnum.values();
//
//        assertArrayEquals(expectedValues, actualValues);
//    }
//
//    @Test
//    void testEnumValueOf() {
//        assertEquals(UserTypeEnum.NURSE, UserTypeEnum.valueOf("NURSE"));
//        assertEquals(UserTypeEnum.DOCTOR, UserTypeEnum.valueOf("DOCTOR"));
//        assertEquals(UserTypeEnum.PATIENT, UserTypeEnum.valueOf("PATIENT"));
//    }
//
//    @Test
//    void testEnumDescriptions() {
//        assertEquals("Enfermeiro", UserTypeEnum.NURSE.getDescription());
//        assertEquals("Médico", UserTypeEnum.DOCTOR.getDescription());
//        assertEquals("Paciente", UserTypeEnum.PATIENT.getDescription());
//    }
//
//    @Test
//    void testInvalidEnumValue() {
//        assertThrows(IllegalArgumentException.class, () -> {
//            UserTypeEnum.valueOf("INVALID");
//        });
//    }
//}
