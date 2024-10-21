package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;

public interface BookService {
    // Mongo
    BookDto saveBook(BookDto bookDto);
    BookDocument findById(String id);
    BookDto findByIdDto(String id);
    BookDto updateBook(String id, BookDto bookDto);
    // JPA

    BookEntity findByIdJPA(Long id);
    BookDto findByIdDtoJPA(Long id);
    BookDto updateBookJPA(Long id, BookDto bookDto);

}
