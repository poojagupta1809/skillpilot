package com.thoughtworks.skillpilot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.skillpilot.exception.*;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.service.EnrollmentService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
@Import(EnrollmentControllerTest.MockConfig.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EnrollmentService enrollmentService() {
            return Mockito.mock(EnrollmentService.class);
        }
    }

    @Test
    void enroll_success() throws Exception {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1);

        when(enrollmentService.enrollLearnerInCourse(1, 101)).thenReturn(enrollment);

        mockMvc.perform(post("/api/enrollments/101/enroll?userId=1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enrollmentId").value(1));
    }

    @Test
    void enroll_duplicate() throws Exception {
        when(enrollmentService.enrollLearnerInCourse(anyInt(), anyInt()))
                .thenThrow(new DuplicateEnrollmentException("Duplicate"));

        mockMvc.perform(post("/api/enrollments/101/enroll?userId=1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Duplicate"));
    }

    @Test
    void enroll_userNotFound() throws Exception {
        when(enrollmentService.enrollLearnerInCourse(anyInt(), anyInt()))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/api/enrollments/101/enroll?userId=1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void unenroll_success() throws Exception {
        when(enrollmentService.unenrollLearnerFromCourse(1, 101)).thenReturn(true);

        mockMvc.perform(delete("/api/enrollments/101/unenroll?userId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrollment deleted successfully"));
    }

    @Test
    void getEnrollmentById_success() throws Exception {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(5);
        when(enrollmentService.getEnrollmentById(5)).thenReturn(Optional.of(enrollment));

        mockMvc.perform(get("/api/enrollments/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentId").value(5));
    }

    @Test
    void getAllEnrollments_success() throws Exception {
        Enrollment e1 = new Enrollment();
        e1.setEnrollmentId(1);
        Enrollment e2 = new Enrollment();
        e2.setEnrollmentId(2);

        when(enrollmentService.getAllEnrollments()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/api/enrollments/getAllEnrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enrollmentId").value(1))
                .andExpect(jsonPath("$[1].enrollmentId").value(2));
    }

    @Test
    void deleteEnrollmentByCourse_success() throws Exception {
        mockMvc.perform(delete("/api/enrollments/course/101"))
                .andExpect(status().isOk())
                .andExpect(content().string("All enrollments for course 101 deleted"));
    }
}
