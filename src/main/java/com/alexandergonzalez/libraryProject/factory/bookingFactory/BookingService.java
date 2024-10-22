package com.alexandergonzalez.libraryProject.factory.bookingFactory;

import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto saveBooking(BookingDto bookingDto);
    List<BookingDto> getLoans();
    List<BookingDto> findByUserId(String id);

}
