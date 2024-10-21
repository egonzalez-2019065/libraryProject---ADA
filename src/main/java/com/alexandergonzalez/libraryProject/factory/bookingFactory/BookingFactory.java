package com.alexandergonzalez.libraryProject.factory.bookingFactory;

import com.alexandergonzalez.libraryProject.services.booking.BookingJPAService;
import com.alexandergonzalez.libraryProject.services.booking.BookingMongoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookingFactory {

    private final BookingJPAService bookingJPAService;
    private final BookingMongoService bookingMongoService;

    @Value("${databaseType}")
    private String databaseType;

    public BookingFactory(BookingJPAService bookingJPAService, BookingMongoService bookingMongoService) {
        this.bookingJPAService = bookingJPAService;
        this.bookingMongoService = bookingMongoService;
    }

    public BookingService getBookingService(){
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return bookingJPAService;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return bookingMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);

    }
}
