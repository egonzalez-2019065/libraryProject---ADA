package com.alexandergonzalez.libraryProject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alexandergonzalez.libraryProject.controllers.BookController;
import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookFactory;
import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookCrontrollerTests {

    @Mock
    private BookFactory bookFactory;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        // Configurar el BookFactory para que devuelva el BookService mockeado
        when(bookFactory.getBookService()).thenReturn(bookService);
    }

    @Test
    void saveBook() {
        // Creamos un BookDto con los datos del libro a guardar
        BookDto bookDto = new BookDto("Title", "Author", "Un libro test", "category test", "123441");

        // Configuramos el comportamiento esperado del servicio
        when(bookService.saveBook(bookDto)).thenReturn(bookDto);

        // Llamamos al método del controlador que guarda el libro
        ResponseEntity<Object> response = bookController.saveBook(bookDto);

        // Verificamos que el libro se haya guardado correctamente y validamos la respuesta
        HashMap<String, BookDto> responseBody = (HashMap<String, BookDto>) response.getBody();
        BookDto savedBook = responseBody.get("Libro creado correctamente");

        // Verificamos que el código de respuesta sea el esperado y que los datos sean correctos
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Title", savedBook.getTitle());
    }

    @Test
    void getBooks() {
        // Simulamos una lista de libros que queremos obtener
        BookDto bookDto = new BookDto("12345", "Title", "Author", "category test", "test", true);
        List<BookDto> books = List.of(bookDto);

        // Configuramos el comportamiento esperado del servicio
        when(bookService.getBooks()).thenReturn(books);

        // Llamamos al método del controlador que obtiene todos los libros
        ResponseEntity<Object> response = bookController.getBooks();

        // Verificamos que la lista de libros se haya obtenido correctamente
        HashMap<String, List<BookDto>> responseBody = (HashMap<String, List<BookDto>>) response.getBody();
        List<BookDto> booksFound = responseBody.get("Libros existentes");

        // Validamos la respuesta y los datos recibidos
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(booksFound);
        assertEquals(1, booksFound.size());
        assertEquals("Title", booksFound.get(0).getTitle());
    }

    @Test
    void getBookById() {
        // Simulamos un libro existente con un ID específico
        BookDto bookDto = new BookDto("12345", "Title", "Author", "category test", "test", true);
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.findByIdDto(id)).thenReturn(bookDto);

        // Llamamos al método del controlador que obtiene un libro por ID
        ResponseEntity<Object> response = bookController.getBookById(id);

        // Verificamos que el libro se haya encontrado correctamente
        HashMap<String, BookDto> responseBody = (HashMap<String, BookDto>) response.getBody();
        BookDto bookFound = responseBody.get("Libro encontrado");

        // Validamos la respuesta y los datos recibidos
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(bookFound);
        assertEquals("Title", bookFound.getTitle());
    }

    @Test
    void getBookByIdNotFound() {
        // Simulamos un caso donde el libro con un ID específico no existe
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.findByIdDto(id)).thenReturn(null);

        // Llamamos al método del controlador que intenta obtener el libro
        ResponseEntity<Object> response = bookController.getBookById(id);

        // Verificamos que no se encontró el libro y que se obtuvo el mensaje correcto
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        Object message = responseBody.get("message");

        // Validamos el código de respuesta y el mensaje
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(message);
        assertEquals("El libro no fue encontrado", message);
    }

    @Test
    void updateBook() {
        // Simulamos un libro existente que se va a actualizar
        BookDto bookDto = new BookDto("12345", "Title", "Author", "category test", "test", true);
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.updateBook(id, bookDto)).thenReturn(bookDto);

        // Llamamos al método del controlador que actualiza el libro
        ResponseEntity<Object> response = bookController.updateBook(id, bookDto);

        // Verificamos que el libro se haya actualizado correctamente
        HashMap<String, BookDto> responseBody = (HashMap<String, BookDto>) response.getBody();
        BookDto bookUpdated = responseBody.get("Libro actualizado");

        // Validamos el código de respuesta y los datos actualizados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(bookUpdated);
        assertEquals("Title", bookUpdated.getTitle());
    }

    @Test
    void updateBookNotFound() {
        // Simulamos un caso donde el libro a actualizar no existe
        BookDto bookDto = new BookDto("12345", "Title", "Author", "category test", "test", true);
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.updateBook(id, bookDto)).thenReturn(null);

        // Llamamos al método del controlador que intenta actualizar el libro
        ResponseEntity<Object> response = bookController.updateBook(id, bookDto);

        // Verificamos que no se pudo actualizar el libro y se obtuvo el mensaje correcto
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        Object message = responseBody.get("message");

        // Validamos el código de respuesta y el mensaje
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(message);
        assertEquals("El libro no pudo ser actualizado", message);
    }

    @Test
    void deleteBook() {
        // Simulamos un libro existente que se va a eliminar
        BookDto bookDto = new BookDto("12345", "Title", "Author", "category test", "test", true);
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.deleteBook(id)).thenReturn(bookDto);

        // Llamamos al método del controlador que elimina el libro
        ResponseEntity<Object> response = bookController.deleteBook(id);

        // Verificamos que el libro se haya eliminado correctamente
        assertEquals(HttpStatus.OK, response.getStatusCode());
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        Object message = responseBody.get("Libro eliminado");

        // Validamos el mensaje de eliminación
        assertNotNull(message);
        assertEquals("Title", message);
    }

    @Test
    void deleteBookNotFound() {
        // Simulamos un caso donde el libro a eliminar no existe
        String id = "12345";

        // Configuramos el comportamiento esperado del servicio
        when(bookService.deleteBook(id)).thenReturn(null);

        // Llamamos al método del controlador que intenta eliminar el libro
        ResponseEntity<Object> response = bookController.deleteBook(id);

        // Verificamos que no se pudo eliminar el libro y se obtuvo el mensaje correcto
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        Object message = responseBody.get("message");

        // Validamos el código de respuesta y el mensaje
        assertNotNull(message);
        assertEquals("El libro no pudo ser eliminado", message);
    }
}