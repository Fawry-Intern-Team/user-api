package com.example.UserAPI.service;

import com.example.UserAPI.DTO.CreateUserRequest;
import com.example.UserAPI.DTO.UserResponse;
import com.example.UserAPI.exception.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.UserAPI.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.UserAPI.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already registered.");
        }

        User user = User.builder()
//                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .password(request.getPassword())
                .isActive(false)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    public UserResponse activateUser(UUID userId) {
        User user = getUserById(userId);
        user.setActive(true);
        return mapToResponse(userRepository.save(user));
    }

    public UserResponse deactivateUser(UUID userId) {
        User user = getUserById(userId);
        user.setActive(false);
        return mapToResponse(userRepository.save(user));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
//                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}