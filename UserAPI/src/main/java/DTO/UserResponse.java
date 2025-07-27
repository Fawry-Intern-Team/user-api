package DTO;

import lombok.Builder;
import lombok.Data;
import model.User.Role;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID userId;
    private String name;
    private String email;
    private Role role;
    private boolean isActive;
}