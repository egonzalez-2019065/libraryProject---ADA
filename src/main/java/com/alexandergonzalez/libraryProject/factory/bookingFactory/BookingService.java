package com.alexandergonzalez.libraryProject.factory.bookingFactory;

import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto saveBooking(BookingDto bookingDto);
    List<BookingDto> getBookings();
    List<BookingDto> findByUserId(String id);
    Boolean deleteBooking(String id);
}
