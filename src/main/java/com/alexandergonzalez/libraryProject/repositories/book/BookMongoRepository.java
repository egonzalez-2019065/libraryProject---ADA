package com.alexandergonzalez.libraryProject.repositories.book;

import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMongoRepository extends MongoRepository<BookDocument, String> {
}
