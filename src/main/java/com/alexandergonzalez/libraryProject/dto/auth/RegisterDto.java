package com.alexandergonzalez.libraryProject.dto.auth;

import com.alexandergonzalez.libraryProject.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class RegisterDto {
    @Id
    private String id;

    private String name;
    private String lastname;
    private String username;
    private String password;
    @JsonIgnore
    private Role role;
    @CreatedDate
    private LocalDateTime createdAt;

    public RegisterDto() {
        this.createdAt = LocalDateTime.now();
    }

    public RegisterDto(String name, String lastname, String username, String password, Role role, LocalDateTime createdAt) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public RegisterDto(String name, String lastname, String username, String password) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }
}
