package com.alexandergonzalez.libraryProject.factory.user;

import com.alexandergonzalez.libraryProject.dto.RoleDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;

import java.util.List;

public interface UserService {
    // Mongo
    UserDto saveUser(UserDto userDto);
    UserDto findByIdDto(String id);
    UserDocument findById(String id);
    UserEntity findByIdJPA(Long id);
    UserDto findByUsername(String name);
    UserDto updateUser(String id, UserDto userDto);
    Boolean updateRole(String id, RoleDto roleDto, String userLogged);
    UserDto deleteUser(String id);
    List<UserDto> getUsers();


}
