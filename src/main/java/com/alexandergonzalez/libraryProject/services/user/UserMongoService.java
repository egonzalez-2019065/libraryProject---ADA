package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.user.UserService;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.user.UserMongoRepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
                user.getUpdatedAt()
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
    public UserEntity findByIdJPA(Long id) {
        return null;
    }

}
