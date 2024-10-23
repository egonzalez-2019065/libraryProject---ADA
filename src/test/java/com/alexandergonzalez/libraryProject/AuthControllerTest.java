package com.alexandergonzalez.libraryProject;

import com.alexandergonzalez.libraryProject.controllers.AuthController;
import com.alexandergonzalez.libraryProject.dto.auth.AuthDto;
import com.alexandergonzalez.libraryProject.dto.auth.LoginDto;
import com.alexandergonzalez.libraryProject.dto.auth.PasswordDto;
import com.alexandergonzalez.libraryProject.dto.auth.RegisterDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.auth.AuthFactory;
import com.alexandergonzalez.libraryProject.factory.auth.AuthService;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthFactory authFactory;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Configurar el AuthFactory para que devuelva el AuthService mockeado
        when(authFactory.getAuthService()).thenReturn(authService);
    }

    private void setupSecurityContext(String username) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void loginSuccess() {
        // Arrange
        LoginDto user = new LoginDto("usernametest", "passwordtest");
        AuthDto expectedAuth = new AuthDto("123444");
        when(authService.login(user)).thenReturn(expectedAuth);

        // Act
        ResponseEntity<AuthDto> response = authController.login(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuth, response.getBody());
        verify(authService).login(user);
    }

    @Test
    void registerSuccess() {
        // Arrange
        RegisterDto registerRequest = new RegisterDto("User", "Test", "usertest", "password");
        RegisterDto userRegistered = new RegisterDto("User", "Test", "usertest", "hashedPassword", Role.ADMIN, LocalDateTime.now());
        when(authService.register(registerRequest)).thenReturn(userRegistered);

        // Act
        ResponseEntity<RegisterDto> response = authController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userRegistered, response.getBody());
        verify(authService).register(registerRequest);
    }

    @Test
    void updatePasswordSuccess() {
        // Arrange
        setupSecurityContext("userupdate");
        String id = "1233";
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");
        UserDto userDto = new UserDto("User", "Test", "userupdate", "oldpasswordtest");

        when(authService.findById(id)).thenReturn(userDto);
        when(authService.updatePassword(id, passwordDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Contraseña actualizada correctamente", responseBody.get("message"));
        verify(authService).updatePassword(id, passwordDto);

        SecurityContextHolder.clearContext();
    }

    @Test
    void updatePasswordError() {
        // Arrange
        setupSecurityContext("userupdate");
        String id = "1233";
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");
        UserDto userDto = new UserDto("User", "Test", "userupdate", "password123");

        when(authService.findById(id)).thenReturn(userDto);
        when(authService.updatePassword(id, passwordDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Assert
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Las contraseñas no coinciden", responseBody.get("message"));
        verify(authService).updatePassword(id, passwordDto);

        SecurityContextHolder.clearContext();
    }

    @Test
    void updatePasswordUnauthorized() {
        // Arrange
        setupSecurityContext("admin");
        String id = "1233";
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");
        UserDto userDto = new UserDto("User", "Test", "userupdate", "oldpasswordtest");

        when(authService.findById(id)).thenReturn(userDto);

        // Act
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("NO puedes actualizar otro usuario que no sea el tuyo", responseBody.get("message"));
        verify(authService, never()).updatePassword(any(), any());

        SecurityContextHolder.clearContext();
    }
}