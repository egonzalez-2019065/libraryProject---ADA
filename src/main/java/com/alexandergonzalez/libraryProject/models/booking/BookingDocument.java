package com.alexandergonzalez.libraryProject.models.booking;

import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import jakarta.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Document(collection = "bookings")
public class BookingDocument {

    @Id
    private String id;

    @DBRef
    private UserDocument userDocument;

    @DBRef
    private BookDocument bookDocument;

    @CreatedDate
    private ZonedDateTime bookingDate;

    private boolean status;

    public BookingDocument() {
        this.bookingDate = ZonedDateTime.now();
        this.status = true;
    }
}
