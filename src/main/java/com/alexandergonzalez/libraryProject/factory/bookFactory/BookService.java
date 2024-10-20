package com.alexandergonzalez.libraryProject.factory.bookFactory;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;

public interface BookService {
    BookDto saveBook(BookDto bookDto);
}
