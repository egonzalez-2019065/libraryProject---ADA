package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.repositories.book.BookMongoRepository;
import com.alexandergonzalez.libraryProject.services.user.BookMongoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookFactory {

    private final BookMongoService bookMongoService;

    @Value("${databaseType}")
    private String databaseType;

    public BookFactory(BookMongoService bookMongoService) {
        this.bookMongoService = bookMongoService;
    }


    public BookService getBookService(){
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return null;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return bookMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);

    }
}
