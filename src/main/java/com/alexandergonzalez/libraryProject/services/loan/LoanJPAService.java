package com.alexandergonzalez.libraryProject.services.loan;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.models.booking.BookingEntity;
import com.alexandergonzalez.libraryProject.models.loan.LoanEntity;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingJPARepository;
import com.alexandergonzalez.libraryProject.repositories.loan.LoanJPARepository;
import com.alexandergonzalez.libraryProject.services.book.BookJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service("jpaLoanService")
public class LoanJPAService implements LoanService {

    private final LoanJPARepository loanJPARepository;
    private final BookingJPARepository bookingJPARepository;
    private final UserJPAService userJPAService;
    private final BookJPAService bookJPAService;


    @Autowired
    public LoanJPAService(LoanJPARepository loanJPARepository, BookingJPARepository bookingJPARepository, UserJPAService userJPAService, BookJPAService bookJPAService) {
        this.loanJPARepository = loanJPARepository;
        this.bookingJPARepository = bookingJPARepository;
        this.userJPAService = userJPAService;
        this.bookJPAService = bookJPAService;
    }

    // Dto para guardar los nuevos préstamos
    private LoanDto toDto(LoanEntity loanEntity){
        return new LoanDto(
                loanEntity.getId(),
                loanEntity.getUserEntity().getId(),
                loanEntity.getBookEntity().getId(),
                loanEntity.getLoanedAt(),
                loanEntity.getReturnDate(),
                loanEntity.getStatus()
        );
    }

    // Dto para obtener los datos (nombres no id's)
    private LoanDto toDtoGet(LoanEntity loanEntity){
        return new LoanDto(
                loanEntity.getId(),
                loanEntity.getUserEntity().getName(),
                loanEntity.getBookEntity().getTitle(),
                loanEntity.getLoanedAt(),
                loanEntity.getReturnDate(),
                loanEntity.getStatus()
        );
    }


    // Método para guardar una nuevo prestamo
    @Override
    public LoanDto saveLoan(LoanDto loanDto) {
        LoanEntity loanEntity = new LoanEntity();

        // Traemos los llaves fóraneas
        UserEntity userDto = userJPAService.findByIdJPA(loanDto.getUserIdJPA());
        BookEntity bookDto = bookJPAService.findByIdJPA(loanDto.getBookIdJPA());

        // Verificamos que las llaves fóraneas no sean nulas
        if (userDto == null || bookDto == null) {
            return null;
        }

        // Verificamos que el libro esté disponible
        if (!bookDto.isAvailable()) {
            return null; // Retornar null si el libro no está disponible
        }

        // Verificamos que no se haya realizado antes el mismo prestamo
        LoanEntity existingLoan = loanJPARepository.findByUserEntity_IdAndBookEntity_IdAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
        if (existingLoan != null) {
            return null;
        }

        // Actualizamos el estado del libro a no disponible
        bookDto.setAvailable(false);
        BookDto bookToDto = bookJPAService.toDto(bookDto);
        bookJPAService.updateBook(String.valueOf(bookToDto.getIdEntity()), bookToDto);

        // Setteamos los valores al nuevo préstamo
        loanEntity.setUserEntity(userDto);
        loanEntity.setBookEntity(bookDto);

        // Guardamos el nuevo préstamo
        loanJPARepository.save(loanEntity);

        // Devolvemos el dto para guardar el préstamo
        return this.toDto(loanEntity);
    }

    // Método para listar los préstamos realizados
    @Override
    public List<LoanDto> getLoans(){
        return loanJPARepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método para devolver el un libro prestado

    @Override
    public LoanDto returnBook(String loanId) {
        // Buscamos el prestamo
        LoanEntity loanFound = loanJPARepository.findById(Long.valueOf(loanId)).orElse(null);

        // Verficamos que si exista el prestamo
        if (loanFound != null) {
            // Buscamos el libro que será devuelto
            BookEntity foundBook = bookJPAService.findByIdJPA(loanFound.getBookEntity().getId());

            // Verificamos que el libro si exista
            if (foundBook != null) {
                // Setteamos los valores para marcar que el libro ha sido devuelto
                loanFound.setReturnDate(LocalDateTime.now());
                loanFound.setStatus(false);
                loanJPARepository.save(loanFound);

                // Setteamos los valores para que el libro quede disponible
                foundBook.setAvailable(true);

                // Verificar si hay reservas para este libro
                List<BookingEntity> activeReservations = bookingJPARepository.findByBookEntity_IdAndStatusTrue(foundBook.getId());
                if (!activeReservations.isEmpty()) {
                    // Tomar la primera reserva (más antigua)
                    BookingEntity firstReservation = activeReservations.stream()
                            .min(Comparator.comparing(BookingEntity::getBookingAt))
                            .orElse(null);

                    if(firstReservation != null) {
                        // Crear un nuevo préstamo para el usuario de la reserva
                        LoanEntity newLoan = new LoanEntity();
                        newLoan.setUserEntity(firstReservation.getUserEntity());
                        newLoan.setBookEntity(foundBook);

                        // Guardar el nuevo préstamo
                        loanJPARepository.save(newLoan);

                        // Opcional: puedes eliminar la reserva o marcarla como usada
                        firstReservation.setStatus(false); // Marcar reserva como inactiva
                        bookingJPARepository.save(firstReservation);

                        // Nuevamente setteamos los valores para que el libro no quede disponible
                        foundBook.setAvailable(false);
                    }
                }

                // Guardamos los datos del libro ya sea quede disponible o no
                BookDto bookToDto = bookJPAService.toDto(foundBook);
                bookJPAService.updateBook(String.valueOf(bookToDto.getIdEntity()), bookToDto);

                // Retornamos el DTO del préstamo
                return this.toDto(loanFound);
            }
            return null;
        }
        return null;
    }


    // Traemos los prestamos para cada usuario
    @Override
    public List<LoanDto> getLoansByUserId(String id) {
        List<LoanEntity> loans = loanJPARepository.findByUserEntity_IdAndStatusTrue(Long.valueOf(id));
        return loans.stream()
                .map(this::toDtoGet)
                .toList();
    }
}
