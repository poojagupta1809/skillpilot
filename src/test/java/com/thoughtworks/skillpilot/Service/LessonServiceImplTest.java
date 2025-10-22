package com.thoughtworks.skillpilot.Service;

import com.thoughtworks.skillpilot.dto.LessonAdminDTO;
import com.thoughtworks.skillpilot.dto.LessonDTO;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.model.ContentType;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import com.thoughtworks.skillpilot.service.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Course course;
    private Lesson lesson;
    private LessonAdminDTO lessonDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId(1);

        lesson = new Lesson();
        lesson.setLessonId(1);
        lesson.setTitle("Lesson");
        lesson.setDescription("This is java");
        lesson.setContentType(ContentType.TEXT);
        lesson.setContent("text");
        lesson.setCourse(course);

        lessonDTO = new LessonAdminDTO();
        lessonDTO.setTitle("Lesson");
        lessonDTO.setDescription("This is java");
        lessonDTO.setContentType("TEXT");
        lessonDTO.setContent("text");
    }


    @Test
    void createLesson_ShouldReturnSavedLesson() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson l = invocation.getArgument(0);
            l.setLessonId(1);
            return l;
        });

        LessonAdminDTO result = lessonService.createLesson(1, lessonDTO);

        assertNotNull(result);
        assertEquals("Lesson", result.getTitle());
        assertEquals(1, result.getLessonId());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void createLesson_ShouldThrowException_WhenCourseNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> lessonService.createLesson(1, lessonDTO));
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    void updateLesson_ShouldUpdateExistingLesson() {
        LessonAdminDTO updateDTO = new LessonAdminDTO();
        updateDTO.setTitle("Updated Lesson");
        updateDTO.setDescription("Updated description");
        updateDTO.setContentType("VIDEO");
        updateDTO.setVideoUrl("http://video.url");
        updateDTO.setContent(null);

        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LessonAdminDTO result = lessonService.updateLesson(1, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Lesson", result.getTitle());
        assertEquals("Updated description", result.getDescription());
        assertEquals("VIDEO", result.getContentType());
        assertEquals("http://video.url", result.getVideoUrl());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void updateLesson_ShouldThrowException_WhenLessonNotFound() {
        when(lessonRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson(1, lessonDTO));
        verify(lessonRepository, never()).save(any(Lesson.class));
    }


    @Test
    void deleteLessonById_ShouldDeleteLesson() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).deleteById(1);

        assertDoesNotThrow(() -> lessonService.deleteLessonById(1));
        verify(lessonRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteLessonById_ShouldThrowException_WhenLessonNotFound() {
        when(lessonRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.deleteLessonById(1));
        verify(lessonRepository, never()).deleteById(anyInt());
    }

    @Test
    void getLessonsByCourseId_ShouldReturnList() {
        when(courseRepository.existsById(1)).thenReturn(true);
        when(lessonRepository.findByCourse_CourseId(1)).thenReturn(Collections.singletonList(lesson));
        List<LessonAdminDTO> lessons = lessonService.getLessonsByCourseId(1);
        assertFalse(lessons.isEmpty());
        assertEquals(1, lessons.size());
        assertEquals("Lesson", lessons.get(0).getTitle());
    }

    @Test
    void getLessonsByCourseId_ShouldReturnEmptyList_WhenNoLessons() {
        when(courseRepository.existsById(1)).thenReturn(true);
        when(lessonRepository.findByCourse_CourseId(1)).thenReturn(Collections.emptyList());
        List<LessonAdminDTO> lessons = lessonService.getLessonsByCourseId(1);
        assertTrue(lessons.isEmpty());
    }

    @Test
    void getLessonsByCourseId_ShouldThrowException_WhenCourseNotFound() {
        when(courseRepository.existsById(1)).thenReturn(false);
        assertThrows(CourseNotFoundException.class, () -> lessonService.getLessonsByCourseId(1));
    }
}