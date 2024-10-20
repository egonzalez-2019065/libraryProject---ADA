package com.alexandergonzalez.libraryProject.controllers;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookFactory;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.HashMap;

@RestController
@RequestMapping("/v1/book")
public class BookController {

    private final BookFactory bookFactory;

    @Autowired
    public BookController(BookFactory bookFactory) {
        this.bookFactory = bookFactory;
    }

    @PostMapping
    public Object saveBook(@RequestBody BookDto bookDto){
        System.out.println(bookDto);
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        BookDto savedBook = bookService.saveBook(bookDto);
        response.put("Libro creado correctamente", savedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
