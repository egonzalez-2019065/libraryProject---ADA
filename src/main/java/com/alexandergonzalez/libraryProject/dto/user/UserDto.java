package com.alexandergonzalez.libraryProject.dto.user;

import com.alexandergonzalez.libraryProject.utils.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private long idEntity;
    private String idDocument;
    private String name;
    private String lastname;
    private String username;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "America/Guatemala")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "America/Guatemala")
    private LocalDateTime updatedAt;
    private String whoUpdatedTo;
    private Role role;

    public UserDto(long idEntity, String name, String lastname, String username, LocalDateTime createdAt, LocalDateTime updatedAt, String whoUpdatedTo) {
        this.idEntity = idEntity;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.whoUpdatedTo = whoUpdatedTo;
    }

    public UserDto(String idDocument, String name, String lastname, String username, LocalDateTime createdAt, LocalDateTime updatedAt, String whoUpdatedTo) {
        this.idDocument = idDocument;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.whoUpdatedTo = whoUpdatedTo;
    }

    public UserDto(String name, String lastname, String username, String password) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public UserDto(String name, String lastname, String username, LocalDateTime updatedAt) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.updatedAt = updatedAt;
    }

}