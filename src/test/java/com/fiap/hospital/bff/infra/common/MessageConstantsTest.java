package com.fiap.hospital.bff.infra.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageConstantsTest {

    @Test
    void testConstantsValues() {
        assertEquals("Restaurant not found with id: ", MessageConstants.RESTAURANT_NOT_FOUND);
        assertEquals("User type not found.", MessageConstants.TYPE_USER_NOT_FOUND);
        assertEquals("Type user not found with id: ", MessageConstants.TYPE_USER_NOT_FOUND_WITH_ID);
        assertEquals("TypeUser ID must be null for creation", MessageConstants.TYPE_USER_ID_NULL);
        assertEquals("User not found with id: ", MessageConstants.USER_NOT_FOUND);
        assertEquals("User not found with email: ", MessageConstants.USER_NOT_FOUND_WITH_EMAIL);
        assertEquals("message", MessageConstants.MESSAGE_ERROR);
        assertEquals("error", MessageConstants.ERROR);
    }

    @Test
    void testPrivateConstructor() throws Exception {
        var constructor = MessageConstants.class.getDeclaredConstructor();
        assertTrue(constructor.canAccess(null) == false);
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
