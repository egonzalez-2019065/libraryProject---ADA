package com.alexandergonzalez.libraryProject.models.booking;


import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @CreatedDate
    private LocalDateTime bookingAt;

    private Boolean status = true;

    public BookingEntity() {
        this.bookingAt = LocalDateTime.now();
    }
}
