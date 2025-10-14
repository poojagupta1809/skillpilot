package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public Lesson createLesson(int courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(int lessonId, Lesson updatedLesson) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id: " + updatedLesson.getLessonId()));
        existingLesson.setTitle(updatedLesson.getTitle());

        if (updatedLesson.getContent() != null) {
            existingLesson.setContent(updatedLesson.getContent());
        }
        return lessonRepository.save(existingLesson);
    }

    public List<Lesson> getLessonsByCourseId(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id " + courseId);
        }

        return lessonRepository.findByCourse_CourseId(courseId);
    }


}
