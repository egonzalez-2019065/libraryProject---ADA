package com.alexandergonzalez.libraryProject.repositories.loan;

import com.alexandergonzalez.libraryProject.models.loan.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {
}
