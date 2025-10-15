package com.thoughtworks.skillpilot.Service;
import com.thoughtworks.skillpilot.exception.*;
import com.thoughtworks.skillpilot.model.*;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import com.thoughtworks.skillpilot.service.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private User user;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1);

        course = new Course();
        course.setCourseId(101);

        enrollment = new Enrollment(user, course);
        enrollment.setEnrollmentId(1001);
    }

    @Test
    void enrollLearnerInCourse_success() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(courseRepository.findById(101)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment saved = enrollmentService.enrollLearnerInCourse(1, 101);

        assertNotNull(saved);
        assertEquals(1001, saved.getEnrollmentId());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enrollLearnerInCourse_duplicateEnrollment() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.of(enrollment));

        assertThrows(DuplicateEnrollmentException.class,
                () -> enrollmentService.enrollLearnerInCourse(1, 101));
    }

    @Test
    void enrollLearnerInCourse_userNotFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> enrollmentService.enrollLearnerInCourse(1, 101));
    }

    @Test
    void enrollLearnerInCourse_courseNotFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(courseRepository.findById(101)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> enrollmentService.enrollLearnerInCourse(1, 101));
    }

    @Test
    void unenrollLearnerFromCourse_success() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.of(enrollment));

        boolean result = enrollmentService.unenrollLearnerFromCourse(1, 101);

        assertTrue(result);
        assertEquals(EnrollmentStatus.UNENROLLED, enrollment.getStatus());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void unenrollLearnerFromCourse_notFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.empty());

        assertThrows(EnrollmentNotFoundException.class,
                () -> enrollmentService.unenrollLearnerFromCourse(1, 101));
    }

    @Test
    void deleteEnrollmentByUserAndCourse_success() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.of(enrollment));

        enrollmentService.deleteEnrollmentByUserAndCourse(1, 101);

        verify(enrollmentRepository, times(1)).delete(enrollment);
    }

    @Test
    void deleteEnrollmentByUserAndCourse_notFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 101))
                .thenReturn(Optional.empty());

        assertThrows(EnrollmentNotFoundException.class,
                () -> enrollmentService.deleteEnrollmentByUserAndCourse(1, 101));
    }
}
