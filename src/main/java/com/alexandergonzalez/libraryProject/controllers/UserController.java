package com.alexandergonzalez.libraryProject.controllers;

import com.alexandergonzalez.libraryProject.dto.RoleDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.user.UserFactory;
import com.alexandergonzalez.libraryProject.factory.user.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserFactory userFactory;

    @Autowired
    public UserController(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @PostConstruct
    public void loadSampleUsers() {
        UserService userService = userFactory.getUserService();
        UserDto found = userService.findByUsername("admin");
        if(found == null) {
            UserDto userAdmin = new UserDto("admin", "admin", "admin", "admin");
            userService.saveUser(userAdmin);
        }
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/save")
    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
        UserService userService = userFactory.getUserService();
        Map<String, Object> response = new HashMap<>();
        UserDto savedUser = userService.saveUser(userDto);
        if(savedUser != null){
            response.put("Usuario guardado correctamente", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("message", "Este nombre de usuario ya existe");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Endpoint que devolverá todos los usuarios exisntentes en la base de datos
    @GetMapping("/")
    public ResponseEntity<Object> getAllUsers() {
        UserService userService = userFactory.getUserService();
        List<UserDto> users = userService.getUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("Usuarios encontrados", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Endpoint que devuelve un usuario por su id
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        UserService userService = userFactory.getUserService();
        Map<String, Object> response = new HashMap<>();
        UserDto userFound = userService.findByIdDto(id);
        if(userFound != null){
            response.put("Usuario encontrado", userFound);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "El usuario que está buscando aún no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Endpoint que actualiza un usuario por su id
    @PutMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        UserService userService = userFactory.getUserService();
        Map<String, Object> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserDto userFound = userService.findByIdDto(id);
        if(userFound != null){
            if(currentPrincipalName.equals(userFound.getUsername())){
                UserDto updatedUser = userService.updateUser(id, userDto);
                response.put("Usuario actualizado", updatedUser);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else{
                response.put("message","NO puedes actualizar otro usuario que no sea el tuyo");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        response.put("message", "El usuario que está buscando aún no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @RolesAllowed("ADMIN")
    @PutMapping("/role/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable String id, @RequestBody RoleDto newRole) {
        UserService userService = userFactory.getUserService();
        Map<String, String> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserDto userFound = userService.findByIdDto(id);
        if(userFound != null){
            Boolean roleUpdated = userService.updateRole(id, newRole, currentPrincipalName);
            if(roleUpdated){
                response.put("message", "Role actualizado correctamente");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            response.put("message","Role no actualizado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        response.put("message", "El usuario que está buscando aún no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Endpoint que elimina un usuario existente por su id
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        UserService userService = userFactory.getUserService();
        Map<String, Object> response = new HashMap<>();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserDto userFound = userService.findByIdDto(id);
        if(userFound != null){
            if(currentPrincipalName.equals(userFound.getUsername())){
                UserDto userDeleted = userService.deleteUser(id);
                response.put("Usuario eliminado", userDeleted.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }else{
                response.put("message","NO puedes eliminar otro usuario que no sea el tuyo");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        response.put("message", "El usuario que está buscando aún no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
