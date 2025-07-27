package com.example.UserAPI.controller;

import com.example.UserAPI.DTO.CreateUserRequest;
import com.example.UserAPI.DTO.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.UserAPI.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User creation and activation endpoints")
public class UserController {

    private final UserService userService;
   
    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Activate a user")
    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }

    @Operation(summary = "Deactivate a user")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    @Operation(summary = "Get all users with pagination")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> listUsers(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
}
