package com.alexandergonzalez.libraryProject.factory.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;

public interface UserService {
    // Mongo
    UserDto saveUser(UserDto userDto);
    UserDto findByIdDto(String id);
    UserDocument findById(String id);
    UserEntity findByIdJPA(Long id);

}
