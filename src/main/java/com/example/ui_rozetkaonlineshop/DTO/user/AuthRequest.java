package com.example.ui_rozetkaonlineshop.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
