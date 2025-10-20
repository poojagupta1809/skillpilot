package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.EnrollmentStatus;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {

    private EnrollmentRepository enrollmentRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    private EnrollmentServiceImpl enrollmentService;

    @BeforeEach
    void setUp() {
        enrollmentRepository = mock(EnrollmentRepository.class);
        userRepository = mock(UserRepository.class);
        courseRepository = mock(CourseRepository.class);
        enrollmentService = new EnrollmentServiceImpl(enrollmentRepository, userRepository, courseRepository);
    }

    @Test
    void enrollLearnerInCourse_success() {
        int userId = 1;
        int courseId = 2;
        User user = new User();
        user.setUserId(userId);
        Course course = new Course();
        course.setCourseId(courseId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Enrollment enrollment = enrollmentService.enrollLearnerInCourse(userId, courseId);

        assertNotNull(enrollment);
        assertEquals(user, enrollment.getUser());
        assertEquals(course, enrollment.getCourse());
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());

        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void enrollLearnerInCourse_userNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);
        Exception ex = assertThrows(UserNotFoundException.class, () -> enrollmentService.enrollLearnerInCourse(1, 2));
        assertEquals("User not found with id: 1", ex.getMessage());
    }

    @Test
    void enrollLearnerInCourse_courseNotFound() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(courseRepository.existsById(2)).thenReturn(false);
        Exception ex = assertThrows(CourseNotFoundException.class, () -> enrollmentService.enrollLearnerInCourse(1, 2));
        assertEquals("Course not found with id: 2", ex.getMessage());
    }

    @Test
    void enrollLearnerInCourse_duplicateEnrollment() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(courseRepository.existsById(2)).thenReturn(true);
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 2)).thenReturn(Optional.of(new Enrollment()));

        Exception ex = assertThrows(DuplicateEnrollmentException.class, () -> enrollmentService.enrollLearnerInCourse(1, 2));
        assertEquals("User 1 is already enrolled in course 2", ex.getMessage());
    }

    @Test
    void unenrollLearnerFromCourse_success() {
        int userId = 1, courseId = 2;
        Enrollment enrollment = new Enrollment();
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        boolean result = enrollmentService.unenrollLearnerFromCourse(userId, courseId);

        assertTrue(result);
        assertEquals(EnrollmentStatus.UNENROLLED, enrollment.getStatus());
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void unenrollLearnerFromCourse_enrollmentNotFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 2)).thenReturn(Optional.empty());
        Exception ex = assertThrows(EnrollmentNotFoundException.class,
                () -> enrollmentService.unenrollLearnerFromCourse(1, 2));
        assertTrue(ex.getMessage().contains("Enrollment not found"));
    }

    @Test
    void getEnrollmentsByCourse_success() {
        int courseId = 2;
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(enrollmentRepository.findByCourse_CourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE)).thenReturn(List.of(new Enrollment()));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);

        assertFalse(enrollments.isEmpty());
    }

    @Test
    void getEnrollmentsByCourse_courseNotFound() {
        when(courseRepository.existsById(2)).thenReturn(false);
        Exception ex = assertThrows(CourseNotFoundException.class, () -> enrollmentService.getEnrollmentsByCourse(2));
        assertEquals("Course not found with id: 2", ex.getMessage());
    }

    @Test
    void getEnrollmentsByUser_success() {
        int userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(enrollmentRepository.findByUser_UserIdAndStatus(userId, EnrollmentStatus.ACTIVE)).thenReturn(List.of(new Enrollment()));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId);

        assertFalse(enrollments.isEmpty());
    }

    @Test
    void getEnrollmentsByUser_userNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);
        Exception ex = assertThrows(UserNotFoundException.class, () -> enrollmentService.getEnrollmentsByUser(1));
        assertEquals("User not found with id: 1", ex.getMessage());
    }

    @Test
    void getEnrollmentById_success() {
        Enrollment enrollment = new Enrollment();
        when(enrollmentRepository.existsById(1)).thenReturn(true);
        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(enrollment));

        Optional<Enrollment> result = enrollmentService.getEnrollmentById(1);

        assertTrue(result.isPresent());
        assertEquals(enrollment, result.get());
    }

    @Test
    void getEnrollmentById_notFound() {
        when(enrollmentRepository.existsById(1)).thenReturn(false);
        Exception ex = assertThrows(EnrollmentNotFoundException.class, () -> enrollmentService.getEnrollmentById(1));
        assertEquals("Enroll not found with id: 1", ex.getMessage());
    }

    @Test
    void getAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(new Enrollment(), new Enrollment()));

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        assertEquals(2, enrollments.size());
    }

    @Test
    void deleteEnrollmentsByCourse_success() {
        int courseId = 3;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        enrollmentService.deleteEnrollmentsByCourse(courseId);

        verify(enrollmentRepository).deleteByCourse_CourseId(courseId);
    }

    @Test
    void deleteEnrollmentsByCourse_courseNotFound() {
        when(courseRepository.existsById(3)).thenReturn(false);
        Exception ex = assertThrows(CourseNotFoundException.class, () -> enrollmentService.deleteEnrollmentsByCourse(3));
        assertEquals("Course not found with id: 3", ex.getMessage());
    }

    @Test
    void deleteEnrollmentByUserAndCourse_success() {
        int userId = 1, courseId = 2;
        Enrollment enrollment = new Enrollment();

        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId)).thenReturn(Optional.of(enrollment));

        enrollmentService.deleteEnrollmentByUserAndCourse(userId, courseId);

        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void deleteEnrollmentByUserAndCourse_notFound() {
        when(enrollmentRepository.findByUser_UserIdAndCourse_CourseId(1, 2)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EnrollmentNotFoundException.class, () -> enrollmentService.deleteEnrollmentByUserAndCourse(1, 2));
        assertEquals("Enrollment not found for userId=1, courseId=2", ex.getMessage());
    }
}
