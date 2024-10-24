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
import java.time.ZonedDateTime;
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
        // Preparamos los datos de entrada: un usuario con su nombre de usuario y contraseña.
        LoginDto user = new LoginDto("usernametest", "passwordtest");

        // Definimos el resultado esperado de la autenticación.
        AuthDto expectedAuth = new AuthDto("123444");

        // Configuramos el comportamiento del servicio para que devuelva el resultado esperado al realizar login.
        when(authService.login(user)).thenReturn(expectedAuth);

        // Llamamos al método de login del controlador.
        ResponseEntity<AuthDto> response = authController.login(user);

        // Comprobamos que la respuesta sea exitosa y que contenga el resultado esperado.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuth, response.getBody());

        // Verificamos que el servicio de autenticación fue llamado con los datos correctos.
        verify(authService).login(user);
    }

    @Test
    void registerSuccess() {
        // Preparamos los datos para el registro de un nuevo usuario.
        RegisterDto registerRequest = new RegisterDto("User", "Test", "usertest", "password");

        // Definimos el objeto que representa al usuario registrado, con un hash para la contraseña.
        RegisterDto userRegistered = new RegisterDto("User", "Test", "usertest", "hashedPassword", Role.ADMIN, ZonedDateTime.now());

        // Configuramos el servicio para que devuelva el usuario registrado al realizar el registro.
        when(authService.register(registerRequest)).thenReturn(userRegistered);

        // Llamamos al método de registro del controlador.
        ResponseEntity<Object> response = authController.register(registerRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Comprobamos que la respuesta sea exitosa.
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(responseBody);

        // Extraemos el usuario registrado del cuerpo de la respuesta.
        RegisterDto returnedUser = (RegisterDto) responseBody.get("Usuario registrado correctamente");

        // Comparamos que el usuario registrado sea el esperado.
        assertEquals(userRegistered, returnedUser);

        // Verificamos que el servicio de registro fue llamado con los datos correctos.
        verify(authService).register(registerRequest);

    }

    @Test
    void updatePasswordSuccess() {
        // Preparamos el contexto de seguridad para simular un usuario autenticado que va a actualizar su contraseña.
        setupSecurityContext("userupdate");
        String id = "1233";

        // Preparamos los datos para la actualización de la contraseña.
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");

        // Creamos un objeto de usuario con la contraseña actual.
        UserDto userDto = new UserDto("User", "Test", "userupdate", "oldpasswordtest");

        // Configuramos el servicio para que devuelva el usuario correspondiente al ID dado.
        when(authService.findById(id)).thenReturn(userDto);

        // Configuramos el servicio para que indique que la actualización de la contraseña fue exitosa.
        when(authService.updatePassword(id, passwordDto)).thenReturn(true);

        // Llamamos al método del controlador que intenta actualizar la contraseña.
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Comprobamos que la respuesta sea exitosa y que el mensaje de éxito esté presente.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Contraseña actualizada correctamente", responseBody.get("message"));

        // Verificamos que el servicio de actualización de contraseña fue llamado con los datos correctos.
        verify(authService).updatePassword(id, passwordDto);

        // Limpiamos el contexto de seguridad después de la prueba.
        SecurityContextHolder.clearContext();
    }

    @Test
    void updatePasswordError() {
        // Preparamos el contexto de seguridad para simular un usuario autenticado que va a actualizar su contraseña.
        setupSecurityContext("userupdate");
        String id = "1233";

        // Preparamos los datos para la actualización de la contraseña.
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");

        // Creamos un objeto de usuario con una contraseña diferente a la actual.
        UserDto userDto = new UserDto("User", "Test", "userupdate", "password123");

        // Configuramos el servicio para que devuelva el usuario correspondiente al ID dado.
        when(authService.findById(id)).thenReturn(userDto);

        // Configuramos el servicio para que indique que la actualización de la contraseña falló.
        when(authService.updatePassword(id, passwordDto)).thenReturn(false);

        // Llamamos al método del controlador que intenta actualizar la contraseña.
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Comprobamos que la respuesta sea un error y que contenga el mensaje correspondiente.
        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Las contraseñas no coinciden", responseBody.get("message"));

        // Verificamos que el servicio de actualización de contraseña fue llamado con los datos correctos.
        verify(authService).updatePassword(id, passwordDto);

        // Limpiamos el contexto de seguridad después de la prueba.
        SecurityContextHolder.clearContext();
    }

    @Test
    void updatePasswordUnauthorized() {
        // Preparamos el contexto de seguridad para simular un usuario que no tiene permiso para actualizar la contraseña.
        setupSecurityContext("admin");
        String id = "1233";

        // Preparamos los datos para la actualización de la contraseña.
        PasswordDto passwordDto = new PasswordDto("oldpasswordtest", "newpasswordtest");

        // Creamos un objeto de usuario con la contraseña actual.
        UserDto userDto = new UserDto("User", "Test", "userupdate", "oldpasswordtest");

        // Configuramos el servicio para que devuelva el usuario correspondiente al ID dado.
        when(authService.findById(id)).thenReturn(userDto);

        // Llamamos al método del controlador que intenta actualizar la contraseña.
        ResponseEntity<Object> response = authController.updatePassword(id, passwordDto);

        // Comprobamos que la respuesta sea un error de autorización y que contenga el mensaje correspondiente.
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("NO puedes actualizar otro usuario que no sea el tuyo", responseBody.get("message"));

        // Verificamos que el servicio de actualización de contraseña no fue llamado.
        verify(authService, never()).updatePassword(any(), any());

        // Limpiamos el contexto de seguridad después de la prueba.
        SecurityContextHolder.clearContext();
    }
}