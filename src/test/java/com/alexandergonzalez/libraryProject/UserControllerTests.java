package com.alexandergonzalez.libraryProject;

import com.alexandergonzalez.libraryProject.controllers.UserController;
import com.alexandergonzalez.libraryProject.dto.RoleDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.user.UserFactory;
import com.alexandergonzalez.libraryProject.factory.user.UserService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Mock
    private UserFactory userFactory;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Configurar el AuthFactory para que devuelva el AuthService mockeado
        when(userFactory.getUserService()).thenReturn(userService);
    }
    // Método auxiliar para configurar el contexto de seguridad cuando sea necesario
    private void setupSecurityContext(String username) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createUser() {
        // Se crea un nuevo usuario de prueba
        UserDto userDto = new UserDto("User", "Test", "usertest", "usertest123");

        // Simulamos el comportamiento del servicio para guardar al usuario
        when(userService.saveUser(userDto)).thenReturn(userDto);

        // Llamamos al controlador para guardar al usuario
        ResponseEntity<Object> response = userController.saveUser(userDto);
        Map<String, UserDto> responseBody = (Map<String, UserDto>) response.getBody();
        UserDto userCreated = responseBody.get("Usuario guardado correctamente");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User", userCreated.getName());
    }

    @Test
    void findUser() {
        // Se prepara un usuario de prueba para buscar
        UserDto userDto = new UserDto("User", "Test", "usertest", "usertest123");
        String id = "1230";

        // Simulamos el comportamiento del servicio para encontrar al usuario
        when(userService.findByIdDto(id)).thenReturn(userDto);

        // Llamamos al controlador para buscar al usuario
        ResponseEntity<Object> response = userController.findById(id);
        Map<String, UserDto> responseBody = (Map<String, UserDto>) response.getBody();
        UserDto userFound = responseBody.get("Usuario encontrado");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(userFound);
        assertEquals("User", userFound.getName());
        assertEquals("usertest", userFound.getUsername());
    }

    @Test
    void findUserError() {
        // Se establece un ID que no corresponde a ningún usuario
        String id = "1230";
        when(userService.findByIdDto(id)).thenReturn(null);

        // Llamamos al controlador para buscar al usuario
        ResponseEntity<Object> response = userController.findById(id);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        Object userNotFound = responseBody.get("message");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(userNotFound);
        assertEquals("El usuario que está buscando aún no existe", userNotFound);
    }

    @Test
    void updateUser() {
        // Configuramos el contexto de seguridad para la actualización
        setupSecurityContext("userupdate");

        // Se prepara un usuario de prueba para actualizar
        UserDto userDto = new UserDto("User", "Update", "userupdate", LocalDateTime.now());
        String id = "123";

        // Simulamos el comportamiento del servicio para encontrar y actualizar al usuario
        when(userService.findByIdDto(id)).thenReturn(userDto);
        when(userService.updateUser(id, userDto)).thenReturn(userDto);

        // Llamamos al controlador para actualizar al usuario
        ResponseEntity<Object> response = userController.updateUser(id, userDto);
        Map<String, UserDto> responseBody = (Map<String, UserDto>) response.getBody();
        UserDto userUpdated = responseBody.get("Usuario actualizado");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(userUpdated);
        assertEquals("userupdate", userUpdated.getUsername());

        // Limpiamos el contexto de seguridad
        SecurityContextHolder.clearContext();
    }

    @Test
    void changeRole() {
        // Configuramos el contexto de seguridad para el cambio de rol
        setupSecurityContext("admin");

        // Se prepara un usuario y el rol a actualizar
        UserDto userDto = new UserDto("User", "Update", "userupdate", LocalDateTime.now());
        RoleDto roleDto = new RoleDto(Role.ADMIN);
        String id = "123";

        // Simulamos el comportamiento del servicio para encontrar al usuario y actualizar su rol
        when(userService.findByIdDto(id)).thenReturn(userDto);
        when(userService.updateRole(id, roleDto, "admin")).thenReturn(true);

        // Llamamos al controlador para cambiar el rol del usuario
        ResponseEntity<Object> response = userController.updateRole(id, roleDto);
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        String roleChanged = responseBody.get("message");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Role actualizado correctamente", roleChanged);

        // Limpiamos el contexto de seguridad
        SecurityContextHolder.clearContext();
    }

    @Test
    void deleteUser() {
        // Configuramos el contexto de seguridad para la eliminación
        setupSecurityContext("userdeleted");

        // Se prepara un usuario de prueba para eliminar
        UserDto userDto = new UserDto("User", "Update", "userdeleted", LocalDateTime.now());
        String id = "123";

        // Simulamos el comportamiento del servicio para encontrar y eliminar al usuario
        when(userService.findByIdDto(id)).thenReturn(userDto);
        when(userService.deleteUser(id)).thenReturn(userDto);

        // Llamamos al controlador para eliminar al usuario
        ResponseEntity<Object> response = userController.deleteUser(id);

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        Object userDeleted = responseBody.get("Usuario eliminado");

        assertNotNull(responseBody);
        assertEquals("userdeleted", userDeleted);

        // Limpiamos el contexto de seguridad
        SecurityContextHolder.clearContext();
    }
}