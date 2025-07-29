package com.example.UserAPI.service;

import com.example.UserAPI.DTO.CreateUserRequest;
import com.example.UserAPI.DTO.UserLoginDTO;
import com.example.UserAPI.DTO.UserResponseDTO;
import com.example.UserAPI.enums.Role;
import com.example.UserAPI.exception.UserException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.UserAPI.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.UserAPI.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already registered.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .roles(request.getRoles() == null ? List.of(Role.USER) : request.getRoles())
                .password(request.getPassword())
                .isActive(true)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    public UserResponseDTO activateUser(UUID userId) {
        User user = getUserById(userId);
        user.setActive(true);
        return mapToResponse(userRepository.save(user));
    }

    public UserResponseDTO deactivateUser(UUID userId) {
        User user = getUserById(userId);
        user.setActive(false);
        return mapToResponse(userRepository.save(user));
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles() == null ? List.of(Role.USER) : user.getRoles())
                .isActive(user.isActive())
                .build();
    }

    public UserResponseDTO verifyUserByEmail(@Valid UserLoginDTO requestUser) {
        User user = userRepository.findByEmail(requestUser.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        System.out.println(requestUser.getPassword() + " " + user.getPassword());
        if(user != null && passwordEncoder.matches(requestUser.getPassword(), user.getPassword()))
            return mapToResponse(user);
        throw new UserException("Email doesn't exist");
    }
}