package com.alexandergonzalez.libraryProject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private long id;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserDto(long id, String name, String lastname, String username, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}