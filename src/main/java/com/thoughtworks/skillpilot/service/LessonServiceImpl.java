package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.LessonAdminDTO;
import com.thoughtworks.skillpilot.dto.LessonDTO;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.model.ContentType;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
  private final LessonRepository lessonRepository;
  private final CourseRepository courseRepository;

  public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository) {
    this.lessonRepository = lessonRepository;
    this.courseRepository = courseRepository;
  }

  @Override
  public LessonAdminDTO createLesson(int courseId, LessonAdminDTO lessonDTO) {
    Course course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () -> new CourseNotFoundException("Course not found with id: " + courseId));
    Lesson lesson = new Lesson();
    lesson.setTitle(lessonDTO.getTitle());
    lesson.setDescription(lessonDTO.getDescription());
    lesson.setContentType(ContentType.valueOf(lessonDTO.getContentType().toUpperCase()));
    lesson.setVideoUrl(lessonDTO.getVideoUrl());
    lesson.setContent(lessonDTO.getContent());
    lesson.setCourse(course);
    Lesson savedLesson = lessonRepository.save(lesson);
    return new LessonAdminDTO(savedLesson);
  }

  @Override
  public LessonAdminDTO updateLesson(int lessonId, LessonAdminDTO lessondto) {
    Lesson existingLesson =
        lessonRepository
            .findById(lessonId)
            .orElseThrow(
                () -> new LessonNotFoundException("Lesson not found with id: " + lessonId));

    if (lessondto.getTitle() != null) existingLesson.setTitle(lessondto.getTitle());
    if (lessondto.getDescription() != null)
      existingLesson.setDescription(lessondto.getDescription());
    if (lessondto.getContent() != null) existingLesson.setContent(lessondto.getContent());
    if (lessondto.getContentType() != null)
      existingLesson.setContentType(ContentType.valueOf(lessondto.getContentType()));
    if (lessondto.getVideoUrl() != null) existingLesson.setVideoUrl(lessondto.getVideoUrl());
    Lesson savedLesson = lessonRepository.save(existingLesson);
    return new LessonAdminDTO(savedLesson);
  }

  @Override
  public void deleteLessonById(int lessonId) {
    Lesson lesson =
        lessonRepository
            .findById(lessonId)
            .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + lessonId));
    lessonRepository.deleteById(lessonId);
  }

  @Override
  public List<LessonAdminDTO> getLessonsByCourseId(int courseId) {
    if (!courseRepository.existsById(courseId)) {
      throw new CourseNotFoundException("Course not found with id " + courseId);
    }
    List<Lesson> lessons = lessonRepository.findByCourse_CourseId(courseId);
    return lessons.stream().map(LessonAdminDTO::new).collect(Collectors.toList());
  }

  @Override
  public List<LessonDTO> getCourseLessonsForLearners(int courseId) {
    if (!courseRepository.existsById(courseId)) {
      throw new CourseNotFoundException("Course not found with id " + courseId);
    }
    List<Lesson> lessons = lessonRepository.findByCourse_CourseId(courseId);
    return lessons.stream().map(LessonDTO::new).collect(Collectors.toList());
  }
  public Lesson getLessonById(int lessonId)
  {
      Optional<Lesson> lesson=lessonRepository.findById(lessonId);
      if(lesson.isPresent())
          return lesson.get();
      else throw new LessonNotFoundException("Lesson not found with id " + lessonId);


  }
}
