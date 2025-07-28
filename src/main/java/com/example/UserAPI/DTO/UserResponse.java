package com.example.UserAPI.DTO;

import com.example.UserAPI.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID userId;
//    private String name;
    private String email;
    private List<Role> roles;
    private boolean isActive;
}