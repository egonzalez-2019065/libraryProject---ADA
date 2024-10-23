package com.alexandergonzalez.libraryProject.repositories.loan;

import com.alexandergonzalez.libraryProject.models.loan.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {
    Optional<LoanDocument> findByUserDocumentAndBookDocumentAndStatusTrue(String user, String book);
    List<LoanDocument> findByUserDocumentAndStatusTrue(String userId);

}
