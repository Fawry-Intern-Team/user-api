package com.example.UserAPI.DTO;

import com.example.UserAPI.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> roles;
    private boolean isActive;
}