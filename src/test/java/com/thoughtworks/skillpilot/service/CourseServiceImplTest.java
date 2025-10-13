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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    private Course course1, course2;

    @BeforeEach
    void setUp() {
        course1 = new Course(1, "Java Course", "Beginner", "JavaTechie", "Java");
        course2 = new Course(2, "Python Course", "Intermediate", "PythonTechie", "Python");
    }

    @Test
    void testGetAllCourses_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseServiceImpl.getAllCourses();

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

        List<Course> result = courseServiceImpl.getFilteredCourses("Java", "Beginner", "JavaTechie");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withTopicAndDifficulty() {
        when(courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase("Java", "Beginner"))
                .thenReturn(Collections.singletonList(course1));

        List<Course> result = courseServiceImpl.getFilteredCourses("Java", "Beginner", "");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withTopicOnly() {
        when(courseRepository.findByTopicIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(course1));

        List<Course> result = courseServiceImpl.getFilteredCourses("Java", "", "");

        assertEquals(1, result.size());
        assertEquals(course1, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withInstructorOnly() {
        when(courseRepository.findByInstructorIgnoreCase("PythonTechie"))
                .thenReturn(Collections.singletonList(course2));

        List<Course> result = courseServiceImpl.getFilteredCourses("", "", "PythonTechie");

        assertEquals(1, result.size());
        assertEquals(course2, result.get(0));
    }

    @Test
    void testGetFilteredCourses_withNoFilters_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseServiceImpl.getFilteredCourses("", "", "");

        assertEquals(2, result.size());
        assertEquals(course1, result.get(0));
        assertEquals(course2, result.get(1));
    }

}
