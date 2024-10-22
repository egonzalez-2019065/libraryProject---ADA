package com.alexandergonzalez.libraryProject.controllers;


import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingFactory;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
            response.put("Reserva creada creado correctamente", bookingToSave);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
            response.put("message", "Reserva no realizada, el libro ya se encuentra reservado por ti o está disponible");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
