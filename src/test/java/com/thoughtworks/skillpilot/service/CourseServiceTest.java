package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course1, course2;

    @BeforeEach
    void setUp() {
        course1 = new Course(1, "Java Course", "Beginner", "JavaTechie", "Java");
        course2 = new Course(2, "Python Course", "Intermediate", "PythonTechie", "Python");
    }

    @Test
    void testGetAllCourses_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals(course1, result.get(0));
        assertEquals(course2, result.get(1));
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetFilteredCourses_withAllFilters() {
        when(courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
                "Java", "JavaTechie", "Beginner"))
                .thenReturn(Collections.singletonList(course1));

        List<Course> result = courseService.getFilteredCourses("Java", "Beginner", "JavaTechie");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withTopicAndDifficulty() {
        when(courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase("Java", "Beginner"))
                .thenReturn(Collections.singletonList(course1));

        List<Course> result = courseService.getFilteredCourses("Java", "Beginner", "");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withTopicOnly() {
        when(courseRepository.findByTopicIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(course1));

        List<Course> result = courseService.getFilteredCourses("Java", "", "");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withInstructorOnly() {
        when(courseRepository.findByInstructorIgnoreCase("PythonTechie"))
                .thenReturn(Collections.singletonList(course2));

        List<Course> result = courseService.getFilteredCourses("", "", "PythonTechie");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withNoFilters_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.getFilteredCourses("", "", "");

        assertEquals(2, result.size());
        assertEquals(course1, result.get(0));
        assertEquals(course2, result.get(1));
    }

    @Test
    void testCreateCourse_returnsNull() {
        Course result = courseService.createCourse(course1);
        assertNull(result); // Currently returns null as per your service
    }

    @Test
    void testRemoveCourseById_doesNothing() {
        assertDoesNotThrow(() -> courseService.removeCourseById(1));
    }
}
