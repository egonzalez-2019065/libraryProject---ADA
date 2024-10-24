package com.alexandergonzalez.libraryProject.controllers;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookFactory;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
public class BookController {

    private final BookFactory bookFactory;

    @Autowired
    public BookController(BookFactory bookFactory) {
        this.bookFactory = bookFactory;
    }

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<Object> saveBook(@RequestBody BookDto bookDto){
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        BookDto savedBook = bookService.saveBook(bookDto);
        response.put("Libro creado correctamente", savedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping()
    public ResponseEntity<Object> getBooks(){
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        List<BookDto> booksFound = bookService.getBooks();
        response.put("Libros existentes", booksFound);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable("id") String id){
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        BookDto bookFound = bookService.findByIdDto(id);
        if(bookFound != null){
            response.put("Libro encontrado", bookFound);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "El libro no fue encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @RolesAllowed("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") String id, @RequestBody BookDto bookDto){
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        BookDto bookUpdated = bookService.updateBook(id, bookDto);
        System.out.println(bookUpdated);
        if(bookUpdated != null) {
            response.put("Libro actualizado", bookUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "El libro no pudo ser actualizado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") String id){
        BookService bookService = bookFactory.getBookService();
        HashMap<String, Object> response = new HashMap<>();
        BookDto bookDeleted = bookService.deleteBook(id);
        if(bookDeleted != null) {
            response.put("Libro eliminado", bookDeleted.getTitle());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "El libro no pudo ser eliminado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }
}
