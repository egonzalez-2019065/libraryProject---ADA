package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.RoleDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.user.UserService;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.user.UserJPARepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service("jpaUserService")
public class UserJPAService implements UserService {

    private final UserJPARepository userJPARepository;

    @Autowired
    public UserJPAService(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    private UserDto toDto(UserEntity user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getWhoUpdatedTo()
        );
    }

    // Declaración del método que guardará en la base de datos un usuario nuevo
    @Override
    public UserDto saveUser(UserDto userDto){
        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRole(Role.ADMIN);
        userJPARepository.save(user);
        return this.toDto(user);
    }

    @Override
    public UserEntity findByIdJPA(Long id) {
        UserEntity userFound = userJPARepository.findById(id).orElse(null);
        if(userFound != null){
            return userFound;
        }
        return null;
    }

    @Override
    public UserDto findByUsername(String name) {
        UserEntity userFound = userJPARepository.findByUsername(name).orElse(null);
        if(userFound != null){
            return this.toDto(userFound);
        }
        return null;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity userFound = userJPARepository.findById(Long.valueOf(id)).orElse(null);
        if(userFound != null){
            userFound.setName(userDto.getName());
            userFound.setLastname(userDto.getLastname());
            userFound.setUsername(userDto.getUsername());
            userFound.setUpdatedAt(ZonedDateTime.now());
            userJPARepository.save(userFound);
            return this.toDto(userFound);
        }
        return null;
    }

    @Override
    public Boolean updateRole(String id, RoleDto roleDto, String userLogged) {
        UserEntity userFound = userJPARepository.findById(Long.valueOf(id)).orElse(null);
        if(userFound != null){
            System.out.println(roleDto.getRole());
            userFound.setRole(roleDto.getRole());
            userFound.setUpdatedAt(ZonedDateTime.now());
            userFound.setWhoUpdatedTo(userLogged);
            userJPARepository.save(userFound);
            return true;
        }
        return false;
    }

    @Override
    public UserDto deleteUser(String id) {
        UserEntity userFound = userJPARepository.findById(Long.valueOf(id)).orElse(null);
        if(userFound != null){
            UserDto userDtoDeleted = toDto(userFound);
            userJPARepository.deleteById(userFound.getId());
            return userDtoDeleted;
        }
        return null;
    }

    @Override
    public List<UserDto> getUsers() {
        return userJPARepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto findByIdDto(String id) {
        UserEntity userDtoFound = findByIdJPA(Long.valueOf(id));
        if(userDtoFound != null){
            return this.toDto(userDtoFound);
        }
        return null;
    }

    @Override
    public UserDocument findById(String id) {
        return null;
    }


}
