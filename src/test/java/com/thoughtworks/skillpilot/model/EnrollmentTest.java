package com.thoughtworks.skillpilot.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    void testEnrollmentDefaultConstructor() {
        Enrollment enrollment = new Enrollment();

        assertNotNull(enrollment);
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertNotNull(enrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentParameterizedConstructor() {
        User user = new User();
        Course course = new Course();
        Enrollment enrollment = new Enrollment(user, course);

        assertEquals(user, enrollment.getUser());
        assertEquals(course, enrollment.getCourse());
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertNotNull(enrollment.getEnrollmentDate());
    }

    @Test
    void testSetAndGetEnrollmentId() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(10);

        assertEquals(10, enrollment.getEnrollmentId());
    }

    @Test
    void testSetAndGetUser() {
        Enrollment enrollment = new Enrollment();
        User user = new User();
        enrollment.setUser(user);

        assertEquals(user, enrollment.getUser());
    }

    @Test
    void testSetAndGetCourse() {
        Enrollment enrollment = new Enrollment();
        Course course = new Course();
        enrollment.setCourse(course);

        assertEquals(course, enrollment.getCourse());
    }

    @Test
    void testSetAndGetStatus() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStatus(EnrollmentStatus.COMPLETED);

        assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
    }

    @Test
    void testSetAndGetCompletionDate() {
        Enrollment enrollment = new Enrollment();
        LocalDateTime now = LocalDateTime.now();
        enrollment.setCompletionDate(now);

        assertEquals(now, enrollment.getCompletionDate());
    }

    @Test
    void testSetAndGetEnrollmentDate() {
        Enrollment enrollment = new Enrollment();
        LocalDateTime date = LocalDateTime.of(2023, 1, 1, 12, 0);
        enrollment.setEnrollmentDate(date);

        assertEquals(date, enrollment.getEnrollmentDate());
    }
}
