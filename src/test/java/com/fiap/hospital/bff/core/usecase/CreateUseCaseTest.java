//package com.fiap.hospital.bff.core.usecase;
//
//import com.fiap.hospital.bff.core.domain.model.user.Type;
//import com.fiap.hospital.bff.core.domain.model.user.User;
//import com.fiap.hospital.bff.core.outputport.SaveGateway;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CreateUseCaseTest {
//
//    private SaveGateway saveGateway;
//    private PasswordEncoder passwordEncoder;
//    private CreateUseCase createUseCase;
//
//    @BeforeEach
//    void setUp() {
//        saveGateway = mock(SaveGateway.class);
//        passwordEncoder = mock(PasswordEncoder.class);
//        createUseCase = new CreateUseCase(saveGateway, passwordEncoder);
//    }
//
//    @Test
//    void testSaveUserSuccessfully() {
//        // Arrange
//        User inputUser = new User("Alice", "alice@example.com", "123456", new Type(1L, "USER", Arrays.asList("ROLE_USER")));
//        String encodedPassword = "encoded123";
//
//        when(passwordEncoder.encode("123456")).thenReturn(encodedPassword);
//        when(saveGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Retorna o user salvo
//
//        // Act
//        User result = createUseCase.save(inputUser);
//
//        // Assert
//        assertEquals(encodedPassword, result.getPassword());
//        verify(passwordEncoder).encode("123456");
//        verify(saveGateway).save(any(User.class));
//    }
//
//    @Test
//    void testPasswordIsEncodedBeforeSaving() {
//        User inputUser = new User("Bob", "bob@example.com", "plainpass", new Type());
//        String encoded = "hashed-pass";
//
//        when(passwordEncoder.encode("plainpass")).thenReturn(encoded);
//        when(saveGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        createUseCase.save(inputUser);
//
//        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        verify(saveGateway).save(captor.capture());
//
//        User savedUser = captor.getValue();
//        assertEquals(encoded, savedUser.getPassword());
//    }
//
//}
