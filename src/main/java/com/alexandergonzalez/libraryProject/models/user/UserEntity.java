package com.alexandergonzalez.libraryProject.models.user;

import com.alexandergonzalez.libraryProject.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String lastname;
    private String username;
    private String password;
    private Role role;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String WhoUpdatedTo;

    public UserEntity() {
        this.createdAt = LocalDateTime.now();
    }
}
