package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.DTO.EnrollmentDTO;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.EnrollmentStatus;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        Course course = new Course();
        course.setCourseId(10);
        course.setTopic("Java Basics");

        enrollment = new Enrollment(user, course);
        enrollment.setEnrollmentId(100);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollmentDTO = new EnrollmentDTO(
                enrollment.getEnrollmentId(),
                user.getUserId(),
                user.getUsername(),
                course.getCourseId(),
                course.getTopic(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }

    @Test
    public void testEnroll_Success() throws Exception {
        when(enrollmentService.enrollLearnerInCourse(1, 10)).thenReturn(enrollment);

        mockMvc.perform(post("/api/enrollments/10/enroll")
                        .param("userId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enrollmentId").value(enrollment.getEnrollmentId()))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.courseId").value(10))
                .andExpect(jsonPath("$.courseTitle").value("Java Basics"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void testEnroll_DuplicateEnrollment() throws Exception {
        when(enrollmentService.enrollLearnerInCourse(1, 10))
                .thenThrow(new DuplicateEnrollmentException("User 1 is already enrolled in course 10"));

        mockMvc.perform(post("/api/enrollments/10/enroll")
                        .param("userId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User 1 is already enrolled in course 10"));
    }

    @Test
    public void testEnroll_UserNotFound() throws Exception {
        when(enrollmentService.enrollLearnerInCourse(999, 10))
                .thenThrow(new UserNotFoundException("User not found with id: 999"));

        mockMvc.perform(post("/api/enrollments/10/enroll")
                        .param("userId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 999"));
    }

    @Test
    public void testUnenroll_Success() throws Exception {
        // Just ensure service call does not throw
        mockMvc.perform(delete("/api/enrollments/10/unenroll")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrollment updated to UNENROLLED"));
    }

    @Test
    public void testUnenroll_EnrollmentNotFound() throws Exception {
        doThrow(new EnrollmentNotFoundException("Enrollment not found for userId=1, courseId=10"))
                .when(enrollmentService).unenrollLearnerFromCourse(1, 10);

        mockMvc.perform(delete("/api/enrollments/10/unenroll")
                        .param("userId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Enrollment not found for userId=1, courseId=10"));
    }

    @Test
    public void testGetEnrollmentById_Found() throws Exception {
        when(enrollmentService.getEnrollmentById(100)).thenReturn(Optional.of(enrollment));

        mockMvc.perform(get("/api/enrollments/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentId").value(100))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.courseId").value(10));
    }

    @Test
    public void testGetEnrollmentById_NotFound() throws Exception {
        when(enrollmentService.getEnrollmentById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enrollments/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Enrollment not found"));
    }

    @Test
    public void testGetEnrollmentsByCourse() throws Exception {
        when(enrollmentService.getEnrollmentsByCourse(10)).thenReturn(List.of(enrollment));

        mockMvc.perform(get("/api/enrollments/course/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enrollmentId").value(enrollment.getEnrollmentId()));
    }

    @Test
    public void testGetEnrollmentsByUser() throws Exception {
        when(enrollmentService.getEnrollmentsByUser(1)).thenReturn(List.of(enrollment));

        mockMvc.perform(get("/api/enrollments/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enrollmentId").value(enrollment.getEnrollmentId()));
    }

    @Test
    public void testDeleteEnrollmentsByCourse() throws Exception {
        mockMvc.perform(delete("/api/enrollments/course/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("All enrollments for course 10 deleted"));
    }

    @Test
    public void testDeleteEnrollmentByUserAndCourse_Success() throws Exception {
        mockMvc.perform(delete("/api/enrollments/course/10/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrollment deleted successfully"));
    }

    @Test
    public void testDeleteEnrollmentByUserAndCourse_NotFound() throws Exception {
        doThrow(new EnrollmentNotFoundException("Enrollment not found for userId=1, courseId=10"))
                .when(enrollmentService).deleteEnrollmentByUserAndCourse(1, 10);

        mockMvc.perform(delete("/api/enrollments/course/10/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Enrollment not found for userId=1, courseId=10"));
    }
}
