package com.alexandergonzalez.libraryProject.factory;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;

public interface UserService {
    // Mongo
    UserDto saveUser(UserDto userDto);
    UserDto findByIdDto(String id);
    UserDocument findById(String id);
    //JPA
    UserEntity findByIdJPA(Long id);
    UserDto findByIdDtoJPA(Long id);



}
