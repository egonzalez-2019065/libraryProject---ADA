package com.alexandergonzalez.libraryProject.services.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.UserService;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.repositories.user.UserMongoRepository;
import com.alexandergonzalez.libraryProject.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        Long uniqueId = System.currentTimeMillis();
        UserDocument user = new UserDocument();
        user.setId(uniqueId);
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setRole(Role.USER);
        userMongoRepository.save(user);
        return this.toDto(user);
    }
}
