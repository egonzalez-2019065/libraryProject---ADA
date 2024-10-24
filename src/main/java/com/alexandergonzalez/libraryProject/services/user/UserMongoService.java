package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.RoleDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.user.UserService;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.user.UserMongoRepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service("mongoUserService")
public class UserMongoService implements UserService {

    private final UserMongoRepository userMongoRepository;

    @Autowired
    public UserMongoService(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    private UserDto toDto(UserDocument user){
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

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserDocument user = new UserDocument();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRole(Role.USER);
        userMongoRepository.save(user);
        return this.toDto(user);
    }

    @Override
    public UserDocument findById(String id) {
        UserDocument userFound = userMongoRepository.findById(id).orElse(null);
        if(userFound != null){
           return userFound;
        }
        return null;
    }

    @Override
    public UserDto findByIdDto(String id){
        UserDocument userFound = findById(id);
        if(userFound != null){
            return this.toDto(userFound);
        }
        return null;
    }


    @Override
    public UserDto findByUsername(String name) {
        UserDocument userFound = userMongoRepository.findByUsername(name).orElse(null);
        if(userFound != null){
            return this.toDto(userFound);
        }
        return null;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserDocument userFound = userMongoRepository.findById(id).orElse(null);
        if(userFound != null){
            userFound.setName(userDto.getName());
            userFound.setLastname(userDto.getLastname());
            userFound.setUsername(userDto.getUsername());
            userFound.setUpdatedAt(ZonedDateTime.now());
            userMongoRepository.save(userFound);
            return this.toDto(userFound);
        }
        return null;
    }

    @Override
    public Boolean updateRole(String id, RoleDto roleDto, String userLogged) {
        UserDocument userFound = userMongoRepository.findById(id).orElse(null);
        if(userFound != null){
            System.out.println(roleDto.getRole());
            userFound.setRole(roleDto.getRole());
            userFound.setUpdatedAt(ZonedDateTime.now());
            userFound.setWhoUpdatedTo(userLogged);
            userMongoRepository.save(userFound);
            return true;
        }
        return false;
    }

    @Override
    public UserDto deleteUser(String id) {
        UserDocument userFound = userMongoRepository.findById(id).orElse(null);
        if(userFound != null){
            UserDto userDtoDeleted = toDto(userFound);
            userMongoRepository.deleteById(id);
            return userDtoDeleted;
        }
        return null;
    }

    @Override
    public List<UserDto> getUsers() {
        return userMongoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserEntity findByIdJPA(Long id) {
        return null;
    }

}
