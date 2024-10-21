package com.alexandergonzalez.libraryProject.services.loan;

import com.alexandergonzalez.libraryProject.dto.book.BookDto;
import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.dto.user.UserDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.loan.LoanDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import com.alexandergonzalez.libraryProject.repositories.loan.LoanMongoRepository;
import com.alexandergonzalez.libraryProject.services.book.BookMongoService;
import com.alexandergonzalez.libraryProject.services.user.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mongoLoanService")
public class LoanMongoService implements LoanService {

    private final LoanMongoRepository loanMongoRepository;
    private final UserMongoService userMongoService;
    private final BookMongoService bookMongoService;



    @Autowired
    public LoanMongoService(LoanMongoRepository loanMongoRepository, UserMongoService userMongoService, BookMongoService bookMongoService) {
        this.loanMongoRepository = loanMongoRepository;
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
    public LoanDto saveLoan(LoanDto loanDto){
        LoanDocument loanDocument = new LoanDocument();
        UserDocument userDto = userMongoService.findById(loanDto.getUserId());
        BookDocument bookDto = bookMongoService.findById(loanDto.getBookId());

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
}
