package com.example.UserAPI.service;

import DTO.CreateUserRequest;
import DTO.UserResponse;
import exception.UserException;
import jakarta.persistence.EntityNotFoundException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;
import repository.UserRepository;
import service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User mockUser = User.builder()
            .userId(UUID.randomUUID())
            .name("Amr Hisham")
            .email("amr@example.com")
            .role(User.Role.CUSTOMER)
            .isActive(false)
            .build();

    // ✅ Test: successful user creation
    @Test
    void createUser_success() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Amr Hisham")
                .email("amr@example.com")
                .role(User.Role.CUSTOMER)
                .build();

        when(userRepository.existsByEmail("amr@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Amr Hisham", response.getName());
        assertEquals("amr@example.com", response.getEmail());
        assertFalse(response.isActive());
    }

    // ✅ Test: email already exists
    @Test
    void createUser_emailAlreadyExists_throwsException() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Amr")
                .email("amr@example.com")
                .role(User.Role.CUSTOMER)
                .build();

        when(userRepository.existsByEmail("amr@example.com")).thenReturn(true);

        UserException exception = assertThrows(UserException.class,
                () -> userService.createUser(request));

        assertEquals("Email already registered.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // ✅ Test: activating user by ID
    @Test
    void activateUser_success() {
        User inactiveUser = mockUser;
        inactiveUser.setActive(false);

        when(userRepository.findById(UUID.randomUUID())).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.activateUser(UUID.randomUUID());

        assertTrue(response.isActive());
        assertEquals("Amr Hisham", response.getName());
    }

    // ✅ Test: pagination for user listing
    @Test
    void getAllUsers_withPagination_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<User> userPage = new PageImpl<>(List.of(mockUser), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponse> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Amr Hisham", result.getContent().get(0).getName());
    }
}