package com.thoughtworks.skillpilot.Service;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import com.thoughtworks.skillpilot.service.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void givenLessonAndCourseId_whenCreateLesson_thenShouldReturnSavedLesson() {
        int courseId = 1;

        Course course = new Course();
        course.setCourseId(courseId);

        Lesson lesson = new Lesson();
        lesson.setTitle("Spring Boot Intro");

        Lesson savedLesson = new Lesson();
        savedLesson.setLessonId(1);
        savedLesson.setTitle("Spring Boot Intro");
        savedLesson.setCourse(course);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any())).thenReturn(savedLesson);
        assertEquals(savedLesson, lessonService.createLesson(courseId, lesson));
        verify(courseRepository, times(1)).findById(courseId);
        verify(lessonRepository, times(1)).save(any());
    }
    @Test
    public void givenNonExistingCourseId_whenCreateLesson_thenShouldThrowException() {
        int courseId = 1;
        Lesson lesson = new Lesson();
        lesson.setTitle("Spring Boot Intro");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> lessonService.createLesson(courseId, lesson));

        verify(lessonRepository, never()).save(any());
    }
    @Test
    public void givenLessonIdAndUpdatedLesson_whenUpdateLesson_thenShouldReturnUpdatedLesson() {
        int lessonId = 1;

        Lesson existingLesson = new Lesson();
        existingLesson.setLessonId(lessonId);
        existingLesson.setTitle("Old Title");
        existingLesson.setContent("Old Content");

        Lesson updatedLesson = new Lesson();
        updatedLesson.setTitle("New Title");
        updatedLesson.setContent("New Content");
        Lesson savedLesson = new Lesson();
        savedLesson.setLessonId(lessonId);
        savedLesson.setTitle("New Title");
        savedLesson.setContent("New Content");

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existingLesson));
        when(lessonRepository.save(existingLesson)).thenReturn(savedLesson);
        assertEquals(savedLesson, lessonService.updateLesson(lessonId, updatedLesson));
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).save(existingLesson);
    }

    @Test
    public void givenNonExistingLessonId_whenUpdateLesson_thenShouldThrowException() {
        int lessonId = 1;

        Lesson updatedLesson = new Lesson();
        updatedLesson.setTitle("New Title");
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson(lessonId, updatedLesson));
        verify(lessonRepository, never()).save(any());
    }
    @Test
    public void givenCourseId_whenCourseExists_thenShouldReturnLessons() {
        int courseId = 1;
        when(courseRepository.existsById(courseId)).thenReturn(true);
        Lesson lesson1 = new Lesson();
        lesson1.setLessonId(1);
        lesson1.setTitle("Lesson 1");

        Lesson lesson2 = new Lesson();
        lesson2.setLessonId(2);
        lesson2.setTitle("Lesson 2");

        List<Lesson> lessons = List.of(lesson1, lesson2);

        when(lessonRepository.findByCourse_CourseId(courseId)).thenReturn(lessons);
        assertEquals(lessons, lessonService.getLessonsByCourseId(courseId));
        verify(courseRepository, times(1)).existsById(courseId);
        verify(lessonRepository, times(1)).findByCourse_CourseId(courseId);
    }

    @Test
    public void givenCourseId_whenCourseDoesNotExist_thenShouldThrowException() {
        int courseId = 1;
        when(courseRepository.existsById(courseId)).thenReturn(false);
        assertThrows(CourseNotFoundException.class, () -> lessonService.getLessonsByCourseId(courseId));
        verify(lessonRepository, never()).findByCourse_CourseId(anyInt());
    }



}