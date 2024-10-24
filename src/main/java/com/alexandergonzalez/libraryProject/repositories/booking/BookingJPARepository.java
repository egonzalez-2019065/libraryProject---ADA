package com.alexandergonzalez.libraryProject.repositories.booking;


import com.alexandergonzalez.libraryProject.models.booking.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingJPARepository extends JpaRepository<BookingEntity, Long> {
    Optional<BookingEntity> findByUserEntity_IdAndBookEntity_IdAndStatusTrue(Long userEntity,  Long bookEntity);
    Optional<BookingEntity> findByIdAndStatusTrue(Long userEntity);

    List<BookingEntity> findByBookEntity_IdAndStatusTrue(Long id);
    List<BookingEntity> findByUserEntity_IdAndStatusTrue(Long id);
}
