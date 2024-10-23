package com.alexandergonzalez.libraryProject.repositories.book;

import com.alexandergonzalez.libraryProject.models.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookEntity, Long> {
}
