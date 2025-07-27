package com.example.UserAPI.DTO;

import lombok.Builder;
import lombok.Data;
import com.example.UserAPI.model.User.Role;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID userId;
//    private String name;
    private String email;
    private Role role;
    private boolean isActive;
}