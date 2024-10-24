package com.alexandergonzalez.libraryProject.services.auth;

import com.alexandergonzalez.libraryProject.config.JwtService;
import com.alexandergonzalez.libraryProject.dto.auth.AuthDto;
import com.alexandergonzalez.libraryProject.dto.auth.LoginDto;
import com.alexandergonzalez.libraryProject.dto.auth.PasswordDto;
import com.alexandergonzalez.libraryProject.dto.auth.RegisterDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.auth.AuthService;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.auth.AuthJPARepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service("jpaAuthService")
public class AuthJPAService implements AuthService {

    private final AuthJPARepository authJPARepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthJPAService(AuthJPARepository authJPARepository) {
        this.authJPARepository = authJPARepository;
    }

    @Override
    public AuthDto login(final LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = authJPARepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return new AuthDto(token);
    }

    @Override
    public RegisterDto register(final RegisterDto request) {
        UserEntity userFound = authJPARepository.findByUsername(request.getUsername()).orElse(null);
        if(userFound == null){
            UserEntity user = new UserEntity();
            user.setName(request.getName());
            user.setLastname(request.getLastname());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);

            authJPARepository.save(user);
            // Mapear los datos del usuario a un UserDto y devolverlo
            RegisterDto userDto = new RegisterDto();
            userDto.setId(String.valueOf(user.getId()));
            userDto.setName(user.getName());
            userDto.setLastname(user.getLastname());
            userDto.setUsername(user.getUsername());
            userDto.setCreatedAt(user.getCreatedAt());

            return userDto;
        }
        return null;
    }

    @Override
    public UserDto findById(String id) {
        UserEntity userFound = authJPARepository.findById(Long.valueOf(id)).orElse(null);
        if(userFound != null){
            return new UserDto(
                    userFound.getId(),
                    userFound.getName(),
                    userFound.getLastname(),
                    userFound.getUsername(),
                    userFound.getCreatedAt(),
                    userFound.getUpdatedAt(),
                    userFound.getWhoUpdatedTo()
            );
        }
        return null;
    }

    @Override
    public Boolean updatePassword(String id, PasswordDto passwordDto) {
        UserEntity userFound = authJPARepository.findById(Long.valueOf(id)).orElse(null);

        if(userFound != null){
            //String oldPasswordEncriptada = (new BCryptPasswordEncoder().encode(passwordDto.getOldPassword()));
            boolean matchPassword = (new BCryptPasswordEncoder().matches(passwordDto.getOldPassword(), userFound.getPassword()));
            System.out.print(matchPassword);
            if(matchPassword){
                userFound.setPassword(new BCryptPasswordEncoder().encode(passwordDto.getNewPassword()));
                userFound.setUpdatedAt(ZonedDateTime.now());
                authJPARepository.save(userFound);
                return true;
            }
            return false;
        }
        return null;
    }

}
