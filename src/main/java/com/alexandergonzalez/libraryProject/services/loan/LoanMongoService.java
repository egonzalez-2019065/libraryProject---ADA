package com.alexandergonzalez.libraryProject.services.loan;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.booking.BookingDocument;
import com.alexandergonzalez.libraryProject.models.loan.LoanDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.repositories.booking.BookingMongoRepository;
import com.alexandergonzalez.libraryProject.repositories.loan.LoanMongoRepository;
import com.alexandergonzalez.libraryProject.services.book.BookMongoService;
import com.alexandergonzalez.libraryProject.services.user.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


@Service("mongoLoanService")
public class LoanMongoService implements LoanService {

    private final LoanMongoRepository loanMongoRepository;
    private final BookingMongoRepository bookingMongoRepository;
    private final UserMongoService userMongoService;
    private final BookMongoService bookMongoService;


    @Autowired
    public LoanMongoService(LoanMongoRepository loanMongoRepository, BookingMongoRepository bookingMongoRepository, UserMongoService userMongoService, BookMongoService bookMongoService) {
        this.loanMongoRepository = loanMongoRepository;
        this.bookingMongoRepository = bookingMongoRepository;
        this.userMongoService = userMongoService;
        this.bookMongoService = bookMongoService;
    }


    // Dto para guardar los datos
    private LoanDto toDto(LoanDocument loanDocument){
        return new LoanDto(
                loanDocument.getId(),
                loanDocument.getUserDocument().getId(),
                loanDocument.getBookDocument().getId(),
                loanDocument.getLoanDate(),
                loanDocument.getReturnDate(),
                loanDocument.isStatus()
        );
    }

    // Dto para obtener los datos (nombres no id's)
    private LoanDto toDtoGet(LoanDocument loanDocument){
        return new LoanDto(
                loanDocument.getId(),
                loanDocument.getUserDocument().getName(),
                loanDocument.getBookDocument().getTitle(),
                loanDocument.getLoanDate(),
                loanDocument.getReturnDate(),
                loanDocument.isStatus()
        );
    }


    // Método para guardar una nuevo prestamo
    @Override
    public LoanDto saveLoan(LoanDto loanDto) {
        LoanDocument loanDocument = new LoanDocument();

        // Traemos los llaves fóraneas
        UserDocument userDto = userMongoService.findById(loanDto.getUserId());
        BookDocument bookDto = bookMongoService.findById(loanDto.getBookId());

        // Verificamos que las llaves fóraneas no sean nulas
        if (userDto == null || bookDto == null) {
            return null;
        }

        // Verificamos que el libro esté disponible
        if (!bookDto.isAvailable()) {
            return null;
        }

        // Verificamos que no se haya realizado antes el mismo prestamo
        LoanDocument existingLoan = loanMongoRepository.findByUserDocumentAndBookDocumentAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
        if (existingLoan != null) {
            return null;
        }

        // Actualizamos el estado del libro a no disponible
        bookDto.setAvailable(false);
        BookDto bookToDto = bookMongoService.toDto(bookDto);
        bookMongoService.updateBook(bookToDto.getIdDocument(), bookToDto);

        // Setteamos los valores al nuevo préstamo
        loanDocument.setUserDocument(userDto);
        loanDocument.setBookDocument(bookDto);

        // Guardamos el nuevo préstamo
        loanMongoRepository.save(loanDocument);

        // Devolvemos el dto para guardar el préstamo
        return this.toDto(loanDocument);
    }


    // Método para listar los préstamos realizados
    @Override
    public List<LoanDto> getLoans(){
        return loanMongoRepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }

    // Método para devolver el un libro prestado
    @Override
    public LoanDto returnBook(String loanId) {
        // Buscamos el prestamo
        LoanDocument loanFound = loanMongoRepository.findById(loanId).orElse(null);

        // Verficamos que si exista el prestamo
        if(loanFound != null){
            // Buscamos el libro que será devuelto
            BookDocument foundBook = bookMongoService.findById(loanFound.getBookDocument().getId());

            // Verificamos que el libro si exista
            if(foundBook != null){
                // Setteamos los valores para marcar que el libro ha sido devuelto
                loanFound.setReturnDate(ZonedDateTime.now());
                loanFound.setStatus(false);
                loanMongoRepository.save(loanFound);

                // Setteamos los valores para que el libro quede disponible
                foundBook.setAvailable(true);

                // Verificar si hay reservas para este libro
                List<BookingDocument> activeReservations = bookingMongoRepository.findByBookDocumentAndStatusTrue(foundBook.getId());
                if (!activeReservations.isEmpty()) {

                    // Tomar la primera reserva
                    BookingDocument firstReservation = activeReservations.get(0);

                    // Crear un nuevo préstamo para el usuario de la reserva
                    LoanDocument newLoan = new LoanDocument();
                    newLoan.setUserDocument(firstReservation.getUserDocument());
                    newLoan.setBookDocument(foundBook);

                    // Guardar el nuevo préstamo
                    loanMongoRepository.save(newLoan);

                    // Desactivamos la reserva porque ya ha sido prestado el libro para esta misma
                    firstReservation.setStatus(false);

                    // Guardamos los datos de la reserva
                    bookingMongoRepository.save(firstReservation);

                    // Nuevamente setteamos los valores para que el libro no quede disponible
                    foundBook.setAvailable(false);

                }

                // Guardamos los datos del libro ya sea quede disponible o no
                BookDto bookToDto = bookMongoService.toDto(foundBook);
                bookMongoService.updateBook(bookToDto.getIdDocument(), bookToDto);

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
        return loanMongoRepository.findByUserDocumentAndStatusTrue(id).stream()
                .map(this::toDtoGet)
                .toList();
    }

}
