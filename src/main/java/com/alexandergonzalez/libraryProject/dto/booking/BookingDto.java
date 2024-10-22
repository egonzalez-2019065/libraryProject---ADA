package com.alexandergonzalez.libraryProject.dto.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {

    private String id;
    private String userId;
    private String bookId;

    private Long idJPA;
    private Long userIdJPA;
    private Long bookIdJPA;
    private LocalDateTime bookingDate;

    public BookingDto(String id, String userId, String bookId, LocalDateTime bookingDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.bookingDate = bookingDate;
    }

    public BookingDto(Long idJPA, Long userIdJPA, Long bookIdJPA, LocalDateTime bookingDate) {
        this.idJPA = idJPA;
        this.userIdJPA = userIdJPA;
        this.bookIdJPA = bookIdJPA;
        this.bookingDate = bookingDate;
    }

    public BookingDto(Long idJPA, String userId, String bookId, LocalDateTime bookingDate) {
        this.idJPA = idJPA;
        this.userId = userId;
        this.bookId = bookId;
        this.bookingDate = bookingDate;
    }
}
