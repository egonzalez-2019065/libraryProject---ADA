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

    private LoanDto toDto(LoanDocument loanDocument){
        return new LoanDto(
                loanDocument.getId(),
                loanDocument.getUserDocument().getId(),
                loanDocument.getBookDocument().getId(),
                loanDocument.getLoanDate(),
                loanDocument.getReturnDate()
        );
    }


    private LoanDto toDtoGet(LoanDocument loanDocument){
        return new LoanDto(
                loanDocument.getId(),
                loanDocument.getUserDocument().getName(),
                loanDocument.getBookDocument().getTitle(),
                loanDocument.getLoanDate(),
                loanDocument.getReturnDate()
        );
    }
    
    @Override
    public LoanDto saveLoan(LoanDto loanDto) {
        LoanDocument loanDocument = new LoanDocument();
        UserDocument userDto = userMongoService.findById(loanDto.getUserId());
        BookDocument bookDto = bookMongoService.findById(loanDto.getBookId());

        if (userDto == null || bookDto == null) {
            return null;
        }


        if (!bookDto.isAvailable()) {
            return null;
        }

        LoanDocument existingLoan = loanMongoRepository.findByUserDocumentAndBookDocumentAndStatusTrue(userDto.getId(), bookDto.getId()).orElse(null);
        if (existingLoan != null) {
            return null;
        }

        bookDto.setAvailable(false);
        BookDto bookToDto = bookMongoService.toDto(bookDto);
        bookMongoService.updateBook(bookToDto.getIdDocument(), bookToDto);

        loanDocument.setUserDocument(userDto);
        loanDocument.setBookDocument(bookDto);

        loanMongoRepository.save(loanDocument);
        return this.toDto(loanDocument);
    }

    @Override
    public List<LoanDto> getLoans(){
        return loanMongoRepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }

    @Override
    public LoanDto returnBook(String loanId) {
        LoanDocument loanFound = loanMongoRepository.findById(loanId).orElse(null);

        System.out.println(loanFound);
        if(loanFound != null){
            BookDocument foundBook = bookMongoService.findById(loanFound.getBookDocument().getId());
            System.out.println(foundBook);
            if(foundBook != null){
                // Setteamos los valores para marcar que el libro ha sido devuelto
                loanFound.setReturnDate(LocalDateTime.now());
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

                    // Opcional: puedes eliminar la reserva o marcarla como usada
                    firstReservation.setStatus(false); // Marcar reserva como inactiva
                    bookingMongoRepository.save(firstReservation);

                    // Nuevamente setteamos los valores para que el libro no quede disponible
                    foundBook.setAvailable(false);

                }

                // Guardamos los datos del libro ya sea quede disponible o no
                BookDto bookToDto = bookMongoService.toDto(foundBook);
                bookMongoService.updateBook(bookToDto.getIdDocument(), bookToDto);

                // Retornamos el dto
                return this.toDto(loanFound);
            }
            return null;
        }
        return null;
    }
    
}
