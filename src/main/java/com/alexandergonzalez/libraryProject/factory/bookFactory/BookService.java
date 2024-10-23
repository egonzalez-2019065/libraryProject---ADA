package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;

import java.util.List;

public interface BookService {
    // Mongo
    BookDto saveBook(BookDto bookDto);
    BookDocument findById(String id);
    BookEntity findByIdJPA(Long id); // Método para encontrar por id para la persistencia JPA
    BookDto findByIdDto(String id);
    BookDto updateBook(String id, BookDto bookDto);
    List<BookDto> getBooks();
    BookDto deleteBook(String id);
}
