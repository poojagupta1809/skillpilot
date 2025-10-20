package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.RoleType;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void registerNewUser_success() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String email = "test@example.com";
        String roleName = "LEARNER";
        RoleType roleType = RoleType.LEARNER;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.registerNewUser(username, password, email, roleName);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerNewUser_duplicateUsername_throwsException() {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () ->
                userService.registerNewUser(username, "password", "email", "LEARNER")
        );
    }

    @Test
    void registerNewUser_invalidRole_throwsException() {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                userService.registerNewUser(username, "password", "email", "INVALID_ROLE")
        );
    }

    @Test
    void registerNewUser_roleNotFound_createsNewRole() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String email = "test@example.com";
        String roleName = "LEARNER";
        RoleType roleType = RoleType.LEARNER;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.registerNewUser(username, password, email, roleName);
      }


}

