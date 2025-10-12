package com.thoughtworks.skillpilot.model;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testNoArgsConstructorAndSettersAndGetters() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setStatus(1);

        assertEquals(1, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals(1, user.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User("testuser", "secret");
        assertEquals("testuser", user.getUsername());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void testRolesManagement() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        roles.add(role);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testEnrollmentsManagement() {
        User user = new User();
        Set<Enrollment> enrollments = new HashSet<>();
        Enrollment enrollment = new Enrollment();
        enrollments.add(enrollment);
        user.setEnrollments(enrollments);
        assertEquals(enrollments, user.getEnrollments());
    }
}

