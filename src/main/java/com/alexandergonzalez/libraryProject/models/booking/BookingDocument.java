package com.alexandergonzalez.libraryProject.models.booking;

import com.alexandergonzalez.libraryProject.models.book.BookDocument;
import com.alexandergonzalez.libraryProject.models.user.UserDocument;
import jakarta.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private LocalDateTime bookingDate;

    private LocalDateTime firstNotification;

    public BookingDocument() {
        this.bookingDate = LocalDateTime.now();
    }
}
