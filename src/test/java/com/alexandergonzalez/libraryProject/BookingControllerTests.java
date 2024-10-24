package com.alexandergonzalez.libraryProject;

import com.alexandergonzalez.libraryProject.controllers.BookingController;
import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingFactory;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {

    @Mock
    private BookingFactory bookingFactory;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        when(bookingFactory.getBookingService()).thenReturn(bookingService);
    }

    @Test
    void saveBooking() {
        // Se prepara una reserva de prueba
        BookingDto bookingDto = new BookingDto("user123", "123", ZonedDateTime.now());

        // Simulamos el comportamiento del servicio para guardar la reserva
        when(bookingService.saveBooking(bookingDto)).thenReturn(bookingDto);

        // Llamamos al controlador para guardar la reserva
        ResponseEntity<Object> response = bookingController.saveBooking(bookingDto);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        BookingDto savedBooking = (BookingDto) responseBody.get("Reserva creada correctamente");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(savedBooking);
        assertEquals("user123", savedBooking.getUserId());
    }

    @Test
    void saveBookingAlreadyReserved() {
        // Se prepara una reserva de prueba
        BookingDto bookingDto = new BookingDto("123", "user123", ZonedDateTime.now());

        // Simulamos el comportamiento del servicio para indicar que la reserva ya existe
        when(bookingService.saveBooking(bookingDto)).thenReturn(null);

        // Llamamos al controlador para intentar guardar la reserva
        ResponseEntity<Object> response = bookingController.saveBooking(bookingDto);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        String errorMessage = (String) responseBody.get("message");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(errorMessage);
        assertEquals("Reserva no realizada, el libro ya se encuentra reservado por ti o está disponible", errorMessage);
    }

    @Test
    void getAllBookings() {
        // Se prepara una lista de reservas de prueba
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(new BookingDto("123", "user123", ZonedDateTime.now()));
        bookings.add(new BookingDto("124", "user124", ZonedDateTime.now()));

        // Simulamos el comportamiento del servicio para retornar las reservas
        when(bookingService.getBookings()).thenReturn(bookings);

        // Llamamos al controlador para obtener todas las reservas
        ResponseEntity<Object> response = bookingController.getAllBookings();
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        List<BookingDto> retrievedBookings = (List<BookingDto>) responseBody.get("Todos los bookings encontrados");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(retrievedBookings);
        assertEquals(2, retrievedBookings.size());
    }

    @Test
    void getBookingsByUser() {
        // Se prepara una lista de reservas de prueba para un usuario específico
        String userId = "user123";
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(new BookingDto(userId, "book123", ZonedDateTime.now()));

        // Simulamos el comportamiento del servicio para retornar las reservas del usuario
        when(bookingService.findByUserId(userId)).thenReturn(bookings);

        // Llamamos al controlador para obtener las reservas del usuario
        ResponseEntity<Object> response = bookingController.getBookingsByUser(userId);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        List<BookingDto> userBookings = (List<BookingDto>) responseBody.get("Bookings encontrados para este usuario");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(userBookings);
        assertEquals(1, userBookings.size());
        assertEquals(userId, userBookings.get(0).getUserId());
    }

    @Test
    void quitBooking() {
        // Se prepara el ID de la reserva a eliminar
        String bookingId = "123";

        // Simulamos el comportamiento del servicio para indicar que la reserva se ha eliminado
        when(bookingService.deleteBooking(bookingId)).thenReturn(true);

        // Llamamos al controlador para quitar la reserva
        ResponseEntity<Object> response = bookingController.quitBooking(bookingId);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        String message = (String) responseBody.get("message");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(message);
        assertEquals("La reserva fue quitada", message);
    }

    @Test
    void quitBookingNotFound() {
        // Se prepara el ID de la reserva a eliminar
        String bookingId = "123";

        // Simulamos el comportamiento del servicio para indicar que la reserva no se ha encontrado
        when(bookingService.deleteBooking(bookingId)).thenReturn(false);

        // Llamamos al controlador para intentar quitar la reserva
        ResponseEntity<Object> response = bookingController.quitBooking(bookingId);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        String errorMessage = (String) responseBody.get("message");

        // Verificamos que la respuesta sea correcta
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(errorMessage);
        assertEquals("No se pudo quitar la reserva, verifica que los datos son correctos", errorMessage);
    }
}
