package com.alexandergonzalez.libraryProject.repositories.user;

import com.alexandergonzalez.libraryProject.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJPARepository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String name);
}
