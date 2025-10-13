package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Role;
import com.thoughtworks.skillpilot.model.RoleType;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.RoleRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository);
    }

    @Test
    void registerNewUser_success() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String email = "test@example.com";
        String roleName = "LEARNER";
        RoleType roleType = RoleType.LEARNER;
        Role role = new Role(roleType);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(roleRepository.findByName(roleType)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.registerNewUser(username, password, email, roleName);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertTrue(user.getRoles().contains(role));
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
        when(roleRepository.findByName(roleType)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.registerNewUser(username, password, email, roleName);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName() == roleType));
    }

    @Test
    void addAdmin_delegatesToRegisterNewUser() {
        UserServiceImpl spyService = spy(userService);
        User user = new User();
        doReturn(user).when(spyService).registerNewUser(any(), any(), any(), eq("ADMIN"));
        assertEquals(user, spyService.addAdmin("u", "p", "e"));
        verify(spyService).registerNewUser("u", "p", "e", "ADMIN");
    }

    @Test
    void addLearner_delegatesToRegisterNewUser() {
        UserServiceImpl spyService = spy(userService);
        User user = new User();
        doReturn(user).when(spyService).registerNewUser(any(), any(), any(), eq("LEARNER"));
        assertEquals(user, spyService.addLearner("u", "p", "e"));
        verify(spyService).registerNewUser("u", "p", "e", "LEARNER");
    }
}

