package com.alexandergonzalez.libraryProject.controllers;


import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingFactory;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/booking")
public class BookingController {

    private final BookingFactory bookingFactory;


    public BookingController(BookingFactory bookingFactory) {
        this.bookingFactory = bookingFactory;
    }

    @PostMapping
    public Object saveBooking(@RequestBody BookingDto bookingDto){
        BookingService bookingService = bookingFactory.getBookingService();
        HashMap<String, Object> response = new HashMap<>();
        BookingDto bookingToSave = bookingService.saveBooking(bookingDto);
        if(bookingToSave != null){
            response.put("Reserva creada correctamente", bookingToSave);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
            response.put("message", "Reserva no realizada, el libro ya se encuentra reservado por ti o está disponible");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllBookings() {
        BookingService bookingService = bookingFactory.getBookingService();
        List<BookingDto> bookingDto = bookingService.getBookings();

        Map<String, Object> response = new HashMap<>();
        response.put("Todos los bookings encontrados", bookingDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getBookingsByUser(@PathVariable("id") String userId) {
        BookingService bookingService = bookingFactory.getBookingService();
        HashMap<String, Object> response = new HashMap<>();

        List<BookingDto> returnedBookings = bookingService.findByUserId(userId);
        response.put("Bookings encontrados para este usuario", returnedBookings);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> quitBooking(@PathVariable("id") String id){
        BookingService bookingService = bookingFactory.getBookingService();
        HashMap<String, Object> response = new HashMap<>();

       Boolean bookingToDelete = bookingService.deleteBooking(id);
       if(bookingToDelete){
           response.put("message", "La reserva fue quitada");
           return ResponseEntity.status(HttpStatus.OK).body(response);
       }
        response.put("message", "No se pudo quitar la reserva, verifica que los datos son correctos");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }

}
