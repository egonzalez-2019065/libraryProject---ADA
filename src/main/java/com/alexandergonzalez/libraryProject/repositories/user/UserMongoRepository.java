package com.alexandergonzalez.libraryProject.repositories.user;

import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String name);

}
