package com.alexandergonzalez.libraryProject.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasswordDto {
    private String oldPassword;
    private String newPassword;


}
