package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.CourseAdminDTO;
import com.thoughtworks.skillpilot.dto.CourseDTO;
import com.thoughtworks.skillpilot.exception.CourseAlreadyExistException;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.service.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    private Course course1, course2, sampleCourse;

    @BeforeEach
    void setUp() {
        course1 = new Course(1, "Java Course", "Beginner", "JavaTechie", "Java");
        course2 = new Course(2, "Python Course", "Intermediate", "PythonTechie", "Python");
    }

    @Test
    void testGetAllCourses_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<CourseDTO> result = courseServiceImpl.getAllCourses();

        assertEquals(2, result.size());
        assertEquals(course1.getCourseId(), result.get(0).getCourseId());
        assertEquals(course1.getTopic(), result.get(0).getTopic());
        assertEquals(course2.getCourseId(), result.get(1).getCourseId());
        assertEquals(course2.getTopic(), result.get(1).getTopic());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetFilteredCourses_withAllFilters() {
        when(courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
                "Java", "JavaTechie", "Beginner"))
                .thenReturn(Collections.singletonList(course1));

        List<CourseDTO> result = courseServiceImpl.getFilteredCourses("Java", "Beginner", "JavaTechie");

        assertEquals(1, result.size());
        assertEquals(course1.getCourseId(), result.get(0).getCourseId());
        assertEquals(course1.getTopic(), result.get(0).getTopic());
    }

    @Test
    void testGetFilteredCourses_withTopicAndDifficulty() {
        when(courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase("Java", "Beginner"))
                .thenReturn(Collections.singletonList(course1));

        List<CourseDTO> result = courseServiceImpl.getFilteredCourses("Java", "Beginner", "");

        assertEquals(1, result.size());
        assertEquals(course1.getCourseId(), result.get(0).getCourseId());
        assertEquals(course1.getTopic(), result.get(0).getTopic());
    }

    @Test
    void testGetFilteredCourses_withTopicOnly() {
        when(courseRepository.findByTopicIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(course1));

        List<CourseDTO> result = courseServiceImpl.getFilteredCourses("Java", "", "");

        assertEquals(1, result.size());
        assertEquals(course1.getCourseId(), result.get(0).getCourseId());
        assertEquals(course1.getTopic(), result.get(0).getTopic());
    }

    @Test
    void testGetFilteredCourses_withInstructorOnly() {
        when(courseRepository.findByInstructorIgnoreCase("PythonTechie"))
                .thenReturn(Collections.singletonList(course2));

        List<CourseDTO> result = courseServiceImpl.getFilteredCourses("", "", "PythonTechie");

        assertEquals(1, result.size());
        assertEquals(course2.getCourseId(), result.get(0).getCourseId());
        assertEquals(course2.getTopic(), result.get(0).getTopic());
    }

    @Test
    void testGetFilteredCourses_withNoFilters_returnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<CourseDTO> result = courseServiceImpl.getFilteredCourses("", "", "");

        assertEquals(2, result.size());
        assertEquals(course1.getCourseId(), result.get(0).getCourseId());
        assertEquals(course2.getCourseId(), result.get(1).getCourseId());
    }

    @Test
    public void createCourse_shouldSaveSuccessfully() {
        sampleCourse = new Course(1,"Spring","Mary","fullStack","Beginner");
        sampleCourse.setCourseId(null);
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        Course result = courseServiceImpl.createCourse(sampleCourse);

        assertNotNull(result);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void createCourse_existingId_shouldThrowException() {
        sampleCourse = new Course(1,"Spring","Mary","fullStack","Beginner");

        assertThrows(CourseAlreadyExistException.class, () -> courseServiceImpl.createCourse(sampleCourse));
    }

    @Test
    public void getAllCourses_shouldReturnList() {
        sampleCourse = new Course(1,"Spring","Mary","fullStack","Beginner");
        when(courseRepository.findAll()).thenReturn(List.of(sampleCourse));

        List<CourseDTO> courses = courseServiceImpl.getAllCourses();

        assertEquals(1, courses.size());
        assertEquals("Spring", courses.get(0).getTopic());
    }

    @Test
    public void removeCourseById_existingId_shouldDelete() {
        sampleCourse = new Course(1,"Spring","Mary","fullStack","Beginner");
        when(courseRepository.existsById(1)).thenReturn(true);
        courseServiceImpl.removeCourseById(1);

        verify(courseRepository, times(1)).deleteById(1);
    }

    @Test
    public void removeCourseById_notFound_shouldThrowException() {
        when(courseRepository.existsById(1)).thenReturn(false);

        assertThrows(CourseNotFoundException.class, () -> {
            courseServiceImpl.removeCourseById(1);
        });
    }

    @Test
    public void updateCourse_existingId_shouldUpdateFields() {
        sampleCourse = new Course(1,"Spring","Mary","fullStack","Beginner");
        Course updateRequest = new Course();
        updateRequest.setDescription("Updated Description");

        when(courseRepository.findById(1)).thenReturn(Optional.of(sampleCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(sampleCourse);

        Course updated = courseServiceImpl.updateCourse(1, updateRequest);

        assertEquals("Updated Description", updated.getDescription());
        verify(courseRepository).save(sampleCourse);
    }

    @Test
    public void updateCourse_notFound_shouldThrowException() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            courseServiceImpl.updateCourse(1, new Course());
        });
    }


}
