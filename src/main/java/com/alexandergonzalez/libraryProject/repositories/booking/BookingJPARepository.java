package com.alexandergonzalez.libraryProject.repositories.booking;


import com.alexandergonzalez.libraryProject.models.booking.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingJPARepository extends JpaRepository<BookingEntity, Long> {
}
