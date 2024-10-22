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

    private LoanDto toDto(LoanEntity loanEntity){
        return new LoanDto(
                loanEntity.getId(),
                loanEntity.getUserEntity().getId(),
                loanEntity.getBookEntity().getId(),
                loanEntity.getLoanedAt(),
                loanEntity.getReturnDate()
        );
    }


    private LoanDto toDtoGet(LoanEntity loanEntity){
        return new LoanDto(
                loanEntity.getId(),
                loanEntity.getUserEntity().getName(),
                loanEntity.getBookEntity().getTitle(),
                loanEntity.getLoanedAt(),
                loanEntity.getReturnDate()
        );
    }

    @Override
    public LoanDto saveLoan(LoanDto loanDto) {
        LoanEntity loanEntity = new LoanEntity();
        UserEntity userDto = userJPAService.findByIdJPA(loanDto.getUserIdJPA());
        BookEntity bookDto = bookJPAService.findByIdJPA(loanDto.getBookIdJPA());

        // Verificar si el usuario o el libro existen
        if (userDto == null || bookDto == null) {
            return null; // Retornar null si el usuario o el libro no existen
        }

        // Verificar si el libro está disponible
        if (!bookDto.isAvailable()) {
            return null; // Retornar null si el libro no está disponible
        }

        // Verificar si ya existe un préstamo activo para el usuario y el libro
        LoanEntity existingLoan = loanJPARepository.findByUserEntity_IdAndBookEntity_IdAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
        if (existingLoan != null) {
            return null; // Retornar null si ya existe un préstamo activo
        }

        // Marcar el libro como no disponible
        bookDto.setAvailable(false);
        BookDto bookToDto = bookJPAService.toDto(bookDto);
        bookJPAService.updateBookJPA(bookToDto.getIdEntity(), bookToDto);

        // Configurar los detalles del nuevo préstamo
        loanEntity.setUserEntity(userDto);
        loanEntity.setBookEntity(bookDto);

        // Guardar el nuevo préstamo
        loanJPARepository.save(loanEntity);
        return this.toDto(loanEntity); // Retornar el DTO del préstamo guardado
    }

    @Override
    public List<LoanDto> getLoans(){
        return loanJPARepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }


    @Override
    public LoanDto returnBook(String loanId) {
        LoanEntity loanFound = loanJPARepository.findById(Long.valueOf(loanId)).orElse(null);

        if (loanFound != null) {
            BookEntity foundBook = bookJPAService.findByIdJPA(loanFound.getBookEntity().getId());

            if (foundBook != null) {
                // Establecer la fecha de retorno y cambiar el estado del préstamo a falso
                loanFound.setReturnDate(LocalDateTime.now());
                loanFound.setStatus(false);
                loanJPARepository.save(loanFound);

                // Marcar el libro como disponible
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

                        // Nuevamente setear el estado del libro a no disponible
                        foundBook.setAvailable(false);
                    }
                }

                // Guardar los datos del libro
                BookDto bookToDto = bookJPAService.toDto(foundBook);
                bookJPAService.updateBookJPA(bookToDto.getIdEntity(), bookToDto);

                // Retornar el DTO del préstamo
                return this.toDto(loanFound);
            }
            return null;
        }
        return null;
    }
}
