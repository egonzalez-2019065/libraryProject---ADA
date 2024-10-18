package com.alexandergonzalez.libraryProject.controllers.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.FactoryDb;
import com.alexandergonzalez.libraryProject.factory.UserFactory;
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

    private final UserFactory userFactory;
    private final String databaseType = "jpa";

    public UserController(UserFactory userFactory) {
        this.userFactory = userFactory;
    }


    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
        UserService userService = userFactory.getUserService(databaseType);
        Map<String, Object> response = new HashMap<>();
        UserDto savedUser = userService.saveUser(userDto);
        response.put("Usuario guardado correctamente", savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
