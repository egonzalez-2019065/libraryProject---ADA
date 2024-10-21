package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.UserService;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.user.UserJPARepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
                user.getUpdatedAt()
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
        user.setRole(Role.USER);
        userJPARepository.save(user);
        return this.toDto(user);
    }

    @Override
    public UserDto findByIdDto(String id) {
        return null;
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
    public UserDto findByIdDtoJPA(Long id) {
        UserEntity userDtoFound = findByIdJPA(id);
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
