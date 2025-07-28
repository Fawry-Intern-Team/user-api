package com.example.UserAPI.DTO;

import com.example.UserAPI.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserDTO {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private List<Role> roles;
}

