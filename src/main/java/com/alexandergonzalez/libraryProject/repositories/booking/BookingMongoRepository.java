package com.alexandergonzalez.libraryProject.repositories.booking;

import com.alexandergonzalez.libraryProject.models.booking.BookingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingMongoRepository extends MongoRepository<BookingDocument, String> {
        Optional<BookingDocument> findByUserDocumentAndBookDocumentAndStatusTrue(String userDocument, String bookDocument);
        Optional<BookingDocument> findByIdAndStatusTrue(String id);

        List<BookingDocument> findByBookDocumentAndStatusTrue(String bookId);
        List<BookingDocument> findByUserDocumentAndStatusTrue(String userId);
}
