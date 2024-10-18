package com.alexandergonzalez.libraryProject.repositories.user;

import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

}
