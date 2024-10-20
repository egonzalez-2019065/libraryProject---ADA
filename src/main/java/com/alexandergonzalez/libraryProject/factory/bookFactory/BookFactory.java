package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.repositories.book.BookMongoRepository;
import com.alexandergonzalez.libraryProject.services.user.BookJPAService;
import com.alexandergonzalez.libraryProject.services.user.BookMongoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookFactory {

    private final BookMongoService bookMongoService;
    private final BookJPAService bookJPAService;

    @Value("${databaseType}")
    private String databaseType;

    public BookFactory(BookMongoService bookMongoService, BookJPAService bookJPAService) {
        this.bookMongoService = bookMongoService;
        this.bookJPAService = bookJPAService;
    }


    public BookService getBookService(){
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return bookJPAService;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return bookMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);

    }
}
