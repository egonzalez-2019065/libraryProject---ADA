package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;

public interface BookService {
    // Mongo
    BookDto saveBook(BookDto bookDto);
    BookDocument findById(String id);
    BookDto findByIdDto(String id);
    // JPA

    BookEntity findByIdJPA(Long id);
    BookDto findByIdDtoJPA(Long id);
}
