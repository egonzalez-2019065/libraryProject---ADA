package com.alexandergonzalez.libraryProject.factory.loanFactory;

import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;

import java.util.List;

public interface LoanService {
    LoanDto saveLoan(LoanDto loanDto);
    List<LoanDto> getLoans();
    LoanDto returnBook(String bookId);
}
