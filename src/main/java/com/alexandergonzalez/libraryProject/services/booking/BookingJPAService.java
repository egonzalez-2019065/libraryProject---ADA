package com.alexandergonzalez.libraryProject.services.booking;


import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.models.booking.BookingEntity;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingJPARepository;
import com.alexandergonzalez.libraryProject.services.book.BookJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import org.springframework.stereotype.Service;

@Service("jpaBookingService")
public class BookingJPAService implements BookingService {

    private final BookingJPARepository bookingJPARepository;
    private final UserJPAService userJPAService;
    private final BookJPAService bookJPAService;

    public BookingJPAService(BookingJPARepository bookingJPARepository, UserJPAService userJPAService, BookJPAService bookJPAService) {
        this.bookingJPARepository = bookingJPARepository;
        this.userJPAService = userJPAService;
        this.bookJPAService = bookJPAService;
    }

    private BookingDto toDto(BookingEntity booking) {
        return new BookingDto(
                booking.getId(),
                booking.getUserEntity().getId(),
                booking.getBookEntity().getId(),
                booking.getBookingAt()
        );
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        BookingEntity bookingToSave = new BookingEntity();
        UserEntity user = userJPAService.findByIdJPA(bookingDto.getUserIdJPA());
        BookEntity book = bookJPAService.findByIdJPA(bookingDto.getBookIdJPA());

        if (!book.isAvailable()) {
            BookingEntity existingBooking = bookingJPARepository.findByUserEntity_IdAndBookEntity_IdAndStatusTrue(user.getId(), book.getId()).orElse(null);

            if (existingBooking == null) {
                bookingToSave.setUserEntity(user);
                bookingToSave.setBookEntity(book);
                bookingJPARepository.save(bookingToSave);
                return this.toDto(bookingToSave);
            }
            return null;
        }
        return null;
    }
}
