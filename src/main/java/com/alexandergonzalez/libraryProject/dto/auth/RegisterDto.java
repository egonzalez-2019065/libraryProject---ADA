package com.alexandergonzalez.libraryProject.dto.auth;

import com.alexandergonzalez.libraryProject.utils.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "America/Guatemala")
    private ZonedDateTime createdAt;

    public RegisterDto() {
        this.createdAt = ZonedDateTime.now();
    }

    public RegisterDto(String name, String lastname, String username, String password, Role role, ZonedDateTime createdAt) {
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
