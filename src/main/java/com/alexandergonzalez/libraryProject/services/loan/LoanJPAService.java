package com.alexandergonzalez.libraryProject.services.loan;

import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.models.loan.LoanEntity;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import com.alexandergonzalez.libraryProject.repositories.loan.LoanJPARepository;
import com.alexandergonzalez.libraryProject.services.book.BookJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jpaLoanService")
public class LoanJPAService implements LoanService {

    private final LoanJPARepository loanJPARepository;
    private final UserJPAService userJPAService;
    private final BookJPAService bookJPAService;


    @Autowired
    public LoanJPAService(LoanJPARepository loanJPARepository, UserJPAService userJPAService, BookJPAService bookJPAService) {
        this.loanJPARepository = loanJPARepository;
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
    public LoanDto saveLoan(LoanDto loanDto){
        LoanEntity loanEntity = new LoanEntity();
        UserEntity userDto = userJPAService.findByIdJPA(loanDto.getUserIdJPA());
        BookEntity bookDto = bookJPAService.findByIdJPA(loanDto.getBookIdJPA());

        loanEntity.setUserEntity(userDto);
        loanEntity.setBookEntity(bookDto);
        loanJPARepository.save(loanEntity);
        return this.toDto(loanEntity);
    }

    @Override
    public List<LoanDto> getLoans(){
        return loanJPARepository.findAll().stream()
                .map(this::toDtoGet)
                .toList();
    }
}
