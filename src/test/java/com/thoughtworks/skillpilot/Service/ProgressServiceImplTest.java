package com.thoughtworks.skillpilot.Service;

import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.exception.ProgressAlreadyExistException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.model.Progress;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import com.thoughtworks.skillpilot.repository.ProgressRepository;
import com.thoughtworks.skillpilot.service.ProgressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProgressServiceImplTest {
    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private Enrollment enrollment;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enrollment = new Enrollment();
        enrollment.setEnrollmentId(1);

        lesson = new Lesson();
        lesson.setLessonId(1);
    }
    @Test
    void testCreateProgressSuccess() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollment_enrollmentIdAndLesson_lessonId(1, 1))
                .thenReturn(null);

        Progress savedProgress = new Progress(enrollment, lesson, false);
        when(progressRepository.save(any(Progress.class))).thenReturn(savedProgress);

        Progress result = progressService.createProgress(1, 1);

        assertNotNull(result);
        assertFalse(result.isCompleted());
        assertEquals(enrollment, result.getEnrollment());
        assertEquals(lesson, result.getLesson());
        verify(progressRepository, times(1)).save(any(Progress.class));
    }
    @Test
    void testCreateProgressEnrollmentNotFound() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EnrollmentNotFoundException.class,
                () -> progressService.createProgress(1, 1));
    }

    @Test
    void testCreateProgressLessonNotFound() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class,
                () -> progressService.createProgress(1, 1));
    }
    @Test
    void testCreateProgressAlreadyExists() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        when(progressRepository.findByEnrollment_enrollmentIdAndLesson_lessonId(1,1))
                .thenReturn(new Progress(enrollment, lesson, false));

        assertThrows(ProgressAlreadyExistException.class,
                () -> progressService.createProgress(1, 1));
    }
}


