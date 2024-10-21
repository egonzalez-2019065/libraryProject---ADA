package com.alexandergonzalez.libraryProject.services.booking;


import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingJPARepository;
import org.springframework.stereotype.Service;

@Service("jpaBookingService")
public class BookingJPAService implements BookingService {

    private final BookingJPARepository bookingJPARepository;

    public BookingJPAService(BookingJPARepository bookingJPARepository) {
        this.bookingJPARepository = bookingJPARepository;
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        return null;
    }
}
