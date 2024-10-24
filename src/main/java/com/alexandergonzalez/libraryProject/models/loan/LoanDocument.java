package com.alexandergonzalez.libraryProject.models.loan;

import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Document(collection = "loans")
public class LoanDocument {

    @Id
    private String id;

    @DBRef
    private UserDocument userDocument;

    @DBRef
    private BookDocument bookDocument;

    @CreatedDate
    private ZonedDateTime loanDate;

    @LastModifiedDate
    private LocalDateTime returnDate;

    private boolean status;

    public LoanDocument() {
        this.loanDate = ZonedDateTime.now();
        this.status = true;
    }
}
