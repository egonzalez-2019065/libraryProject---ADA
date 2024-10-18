package com.alexandergonzalez.libraryProject.factory;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;

public interface UserService {
    UserDto saveUser(UserDto userDto);
}
