package com.alexandergonzalez.libraryProject.repositories.loan;

import com.alexandergonzalez.libraryProject.models.loan.LoanDocument;
import com.alexandergonzalez.libraryProject.models.loan.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanJPARepository extends JpaRepository<LoanEntity, Long> {
    Optional<LoanEntity> findByUserEntity_IdAndBookEntity_IdAndStatusTrue(Long user, Long book);
    List<LoanEntity> findByUserEntity_IdAndStatusTrue(Long userId);

}
