package com.alexandergonzalez.libraryProject.dto.loan;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanDto {

    private String id;
    private String userId;
    private String bookId;

    private Long idJPA;
    private Long userIdJPA;
    private Long bookIdJPA;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    public LoanDto(String id, String userId, String bookId, LocalDateTime loanDate, LocalDateTime returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public LoanDto(Long idJPA, Long userIdJPA, Long bookIdJPA, LocalDateTime loanDate, LocalDateTime returnDate) {
        this.idJPA = idJPA;
        this.userIdJPA = userIdJPA;
        this.bookIdJPA = bookIdJPA;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public LoanDto(Long idJPA, String userId, String bookId, LocalDateTime loanDate, LocalDateTime returnDate) {
        this.idJPA = idJPA;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }
}
