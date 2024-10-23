package com.alexandergonzalez.libraryProject.controllers;

import com.alexandergonzalez.libraryProject.dto.auth.AuthDto;
import com.alexandergonzalez.libraryProject.dto.auth.LoginDto;
import com.alexandergonzalez.libraryProject.dto.auth.PasswordDto;
import com.alexandergonzalez.libraryProject.dto.auth.RegisterDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.auth.AuthFactory;
import com.alexandergonzalez.libraryProject.factory.auth.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {


    private final AuthFactory authFactory;

    public AuthController(AuthFactory authFactory) {
        this.authFactory = authFactory;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto login){
        AuthService authService = authFactory.getAuthService();
        AuthDto authDto = authService.login(login);
        return ResponseEntity.ok(authDto);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto dto){
        AuthService authService = authFactory.getAuthService();
        RegisterDto userDto = authService.register(dto);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable String id, @RequestBody PasswordDto passwordDto) {
        AuthService authService = authFactory.getAuthService();
        Map<String, String> response = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserDto userFound = authService.findById(id);
        if(userFound != null){
            if(currentPrincipalName.equals(userFound.getUsername())){
                Boolean updatedUser = authService.updatePassword(id, passwordDto);
                if(updatedUser){
                    response.put("message", "Contraseña actualizada correctamente");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                response.put("message","Las contraseñas no coinciden");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
            }
            response.put("message","NO puedes actualizar otro usuario que no sea el tuyo");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response.put("message", "El usuario que está buscando aún no existe");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
