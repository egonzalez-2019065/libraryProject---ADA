package com.alexandergonzalez.libraryProject.repositories.auth;

import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String username);

}
