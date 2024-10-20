package com.alexandergonzalez.libraryProject.controllers.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.FactoryDb;
import com.alexandergonzalez.libraryProject.factory.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(FactoryDb serviceFactory) {
        this.userService = serviceFactory.getService(UserService.class);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto){
        Map<String, Object> response = new HashMap<>();
        UserDto userToSave = userService.saveUser(userDto);
        response.put("Usuario guardado correctamente", userToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
