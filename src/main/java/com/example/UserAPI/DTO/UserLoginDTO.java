package com.example.UserAPI.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDTO {
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
