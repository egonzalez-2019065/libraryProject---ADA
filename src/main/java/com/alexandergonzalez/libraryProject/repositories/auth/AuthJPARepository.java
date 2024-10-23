package com.alexandergonzalez.libraryProject.repositories.auth;

import com.alexandergonzalez.libraryProject.models.user.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthJPARepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

}