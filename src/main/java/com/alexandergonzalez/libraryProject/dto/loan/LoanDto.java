package com.alexandergonzalez.libraryProject.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "America/Guatemala")
    private ZonedDateTime loanDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "America/Guatemala")
    private ZonedDateTime returnDate;

    private boolean status;

    // Constructores con ZonedDateTime
    public LoanDto(String id, String userId, String bookId, ZonedDateTime loanDate, ZonedDateTime returnDate, boolean status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public LoanDto(Long idJPA, Long userIdJPA, Long bookIdJPA, ZonedDateTime loanDate, ZonedDateTime returnDate, boolean status) {
        this.idJPA = idJPA;
        this.userIdJPA = userIdJPA;
        this.bookIdJPA = bookIdJPA;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public LoanDto(Long idJPA, String userId, String bookId, ZonedDateTime loanDate, ZonedDateTime returnDate, boolean status) {
        this.idJPA = idJPA;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public LoanDto(String userId, String bookId, ZonedDateTime loanDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
    }

}

