package com.alexandergonzalez.libraryProject.models.user;

import com.alexandergonzalez.libraryProject.utils.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document(collection = "users")
public class UserDocument {
    @Id
    private long id;

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

    public UserDocument() {
        this.createdAt = LocalDateTime.now();
    }
}
