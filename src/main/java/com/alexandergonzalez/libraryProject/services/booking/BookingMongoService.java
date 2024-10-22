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

import java.util.List;

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

    // Dto para guardar una nueva reserva
    private BookingDto toDto(BookingDocument bookingDocument){
        return new BookingDto(
                bookingDocument.getId(),
                bookingDocument.getUserDocument().getId(),
                bookingDocument.getBookDocument().getId(),
                bookingDocument.getBookingDate(),
                bookingDocument.isStatus()
        );
    }

    // Dto para listar las reservas
    private BookingDto toDtoGet(BookingDocument bookingDocument){
        return new BookingDto(
                bookingDocument.getId(),
                bookingDocument.getUserDocument().getName(),
                bookingDocument.getBookDocument().getTitle(),
                bookingDocument.getBookingDate(),
                bookingDocument.isStatus()
        );
    }

    // Método para guardar una nueva reserva
    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        BookingDocument bookingToSave = new BookingDocument();

        // Buscamos las llaves foraneas
        UserDocument userDto = userMongoService.findById(bookingDto.getUserId());
        BookDocument bookDto = bookMongoService.findById(bookingDto.getBookId());

        // Verificamos que las llaves fóraneas no sean nulas
        if (userDto == null || bookDto == null) {
            return null;
        }

        System.out.println(bookDto);
        // Verificamos que el libro no esté disponible
        if(!bookDto.isAvailable()) {

            // Verificamos que no haya una reserva para este libro con este usuario
            BookingDocument existingBooking = bookingMongoRepository.findByUserDocumentAndBookDocumentAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
            if(existingBooking == null){

                // Setteamos los datos
                bookingToSave.setUserDocument(userDto);
                bookingToSave.setBookDocument(bookDto);

                // Guardamos la nueva reserva
                bookingMongoRepository.save(bookingToSave);

                // Devolvemos la nueva reserva
                return this.toDto(bookingToSave);
            }
            return null;
        }
        return null;
    }

    // Método que lista todas las reservas
    @Override
    public List<BookingDto> getBookings() {
        return bookingMongoRepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método que retorna las reservas por usuario
    @Override
    public List<BookingDto> findByUserId(String id) {
        return bookingMongoRepository.findByUserDocumentAndStatusTrue(id).stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método que permite desactivar reservas
    @Override
    public Boolean deleteBooking(String id) {
        // Buscamos a la reserva que exista y esté activa
        BookingDocument bookingFound = bookingMongoRepository.findByIdAndStatusTrue(id).orElse(null);

        // Verificamos que los datos sean correctos
        if(bookingFound != null){

            // Settea el valor a inactivo
            bookingFound.setStatus(false);

            // Guarda los cambios
            bookingMongoRepository.save(bookingFound);

            // Retorna true para la respuesta
            return true;
        }
        // Retorna false para la respuesta
        return false;
    }
}
