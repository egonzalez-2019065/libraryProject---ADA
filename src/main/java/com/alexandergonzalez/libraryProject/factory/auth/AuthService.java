package com.alexandergonzalez.libraryProject.factory.auth;

import com.alexandergonzalez.libraryProject.dto.auth.AuthDto;
import com.alexandergonzalez.libraryProject.dto.auth.LoginDto;
import com.alexandergonzalez.libraryProject.dto.auth.PasswordDto;
import com.alexandergonzalez.libraryProject.dto.auth.RegisterDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;

import java.util.Optional;

public interface AuthService {
    AuthDto login(final LoginDto request);
    RegisterDto register(final RegisterDto request);
    UserDto findById(String id);
    Boolean updatePassword(String id, PasswordDto passwordDto);
}
