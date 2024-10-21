package com.alexandergonzalez.libraryProject.repositories.loan;

import com.alexandergonzalez.libraryProject.models.loan.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanJPARepository extends JpaRepository<LoanEntity, Long> {
}
