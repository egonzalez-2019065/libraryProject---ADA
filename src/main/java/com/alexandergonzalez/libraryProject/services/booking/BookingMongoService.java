package com.alexandergonzalez.libraryProject.services.booking;

import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.booking.BookingDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingMongoRepository;
import com.alexandergonzalez.libraryProject.services.book.BookMongoService;
import com.alexandergonzalez.libraryProject.services.user.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mongoBookingService")
public class BookingMongoService implements BookingService {

    private final BookingMongoRepository bookingMongoRepository;
    private final UserMongoService userMongoService;
    private final BookMongoService bookMongoService;

    @Autowired
    public BookingMongoService(BookingMongoRepository bookingMongoRepository, UserMongoService userMongoService, BookMongoService bookMongoService) {
        this.bookingMongoRepository = bookingMongoRepository;
        this.userMongoService = userMongoService;
        this.bookMongoService = bookMongoService;
    }

    private BookingDto toDto(BookingDocument bookingDocument){
        return new BookingDto(
                bookingDocument.getId(),
                bookingDocument.getUserDocument().getId(),
                bookingDocument.getBookDocument().getId(),
                bookingDocument.getBookingDate()
        );
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        BookingDocument bookToSave = new BookingDocument();
        UserDocument userDto = userMongoService.findById(bookingDto.getUserId());
        BookDocument bookDto = bookMongoService.findById(bookingDto.getBookId());
        System.out.println(bookDto);

        if(!bookDto.isAvailable()) {
            BookingDocument existingBooking = bookingMongoRepository.findByUserDocumentAndBookDocumentAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
            if(existingBooking == null){
                bookToSave.setUserDocument(userDto);
                bookToSave.setBookDocument(bookDto);
                bookingMongoRepository.save(bookToSave);
                return this.toDto(bookToSave);
            }
            return null;
        }
        return null;
    }
}
