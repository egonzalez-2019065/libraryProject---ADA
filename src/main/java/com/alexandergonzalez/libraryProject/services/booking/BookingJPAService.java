package com.alexandergonzalez.libraryProject.services.booking;


import com.alexandergonzalez.libraryProject.dto.booking.BookingDto;
import com.alexandergonzalez.libraryProject.factory.bookingFactory.BookingService;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.models.booking.BookingEntity;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingJPARepository;
import com.alexandergonzalez.libraryProject.services.book.BookJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jpaBookingService")
public class BookingJPAService implements BookingService {

    private final BookingJPARepository bookingJPARepository;
    private final UserJPAService userJPAService;
    private final BookJPAService bookJPAService;

    @Autowired
    public BookingJPAService(BookingJPARepository bookingJPARepository, UserJPAService userJPAService, BookJPAService bookJPAService) {
        this.bookingJPARepository = bookingJPARepository;
        this.userJPAService = userJPAService;
        this.bookJPAService = bookJPAService;
    }

    // Dto para guardar una nueva reserva
    private BookingDto toDto(BookingEntity booking) {
        return new BookingDto(
                booking.getId(),
                booking.getUserEntity().getId(),
                booking.getBookEntity().getId(),
                booking.getBookingAt(),
                booking.getStatus()
        );
    }

    // Dto para listar las reservas
    private BookingDto toDtoGet(BookingEntity booking) {
        return new BookingDto(
                booking.getId(),
                booking.getUserEntity().getName(),
                booking.getBookEntity().getTitle(),
                booking.getBookingAt(),
                booking.getStatus()
        );
    }

    // Método para guardar una nueva reserva
    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        BookingEntity bookingToSave = new BookingEntity();

        // Buscamos las llaves foraneas
        UserEntity user = userJPAService.findByIdJPA(bookingDto.getUserIdJPA());
        BookEntity book = bookJPAService.findByIdJPA(bookingDto.getBookIdJPA());


        // Verificamos que las llaves fóraneas no sean nulas
        if (user == null || book == null) {
            return null;
        }

        // Verificamos que el libro no esté disponible
        if (!book.isAvailable()) {

            // Verificamos que no haya una reserva para este libro con este usuario
            BookingEntity existingBooking = bookingJPARepository.findByUserEntity_IdAndBookEntity_IdAndStatusTrue(user.getId(), book.getId()).orElse(null);

            if (existingBooking == null) {

                // Setteamos los datos
                bookingToSave.setUserEntity(user);
                bookingToSave.setBookEntity(book);

                // Guardamos la nueva reserva
                bookingJPARepository.save(bookingToSave);

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
        return bookingJPARepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método que retorna las reservas por usuario
    @Override
    public List<BookingDto> findByUserId(String id) {
        return bookingJPARepository.findByUserEntity_IdAndStatusTrue(Long.parseLong(id)).stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método que permite desactivar reservas
    @Override
    public Boolean deleteBooking(String id) {
        // Buscamos a la reserva que exista y esté activa
        BookingEntity bookingFound = bookingJPARepository.findByIdAndStatusTrue(Long.valueOf(id)).orElse(null);

        // Verificamos que los datos sean correctos
        if(bookingFound != null){

            // Settea el valor a inactivo
            bookingFound.setStatus(false);

            // Guarda los cambios
            bookingJPARepository.save(bookingFound);

            // Retorna true para la respuesta
            return true;
        }
        // Retorna false para la respuesta
        return false;
    }
}
