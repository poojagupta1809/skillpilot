package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.CourseDTO;
import com.thoughtworks.skillpilot.exception.CourseAlreadyExistException;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

  private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
  @Autowired private CourseRepository courseRepository;

  @Override
  public Course createCourse(Course course) {
    if (course.getCourseId() != null) {
      throw new CourseAlreadyExistException("Course with this ID already exists!");
    }
    course.setCourseId(null);
    return courseRepository.save(course);
  }

  @Override
  public void removeCourseById(int courseId) {
    boolean isCourseExist = courseRepository.existsById(courseId);
    if (isCourseExist) {
      try {
        courseRepository.deleteById(courseId);
      } catch (RuntimeException e) {
        log.error(e.getMessage());
      }
    } else {
      throw new CourseNotFoundException("Course with ID " + courseId + " not found");
    }
  }

  @Override
  public List<CourseDTO> getAllCourses() {
    return courseRepository.findAll().stream().map(CourseDTO::new).collect(Collectors.toList());
  }

  public Course updateCourse(int id, Course course) {
    Optional<Course> existingCourse = courseRepository.findById(id);
    if (existingCourse.isPresent()) {
      Course courseToUpdate = existingCourse.get();
      if (course.getDescription() != null) {
        courseToUpdate.setDescription(course.getDescription());
      }
      if (course.getTopic() != null) {
        courseToUpdate.setTopic(course.getTopic());
      }
      if (course.getDifficultyLevel() != null) {
        courseToUpdate.setDifficultyLevel(course.getDifficultyLevel());
      }
      if (course.getInstructor() != null) {
        courseToUpdate.setInstructor(course.getInstructor());
      }
      if (course.getImageUrl() != null) {
            courseToUpdate.setImageUrl(course.getImageUrl());
      }
      try {
        courseRepository.save(courseToUpdate);
        return courseToUpdate;
      } catch (RuntimeException e) {
        log.error(e.getMessage());
        return null;
      }
    } else throw new CourseNotFoundException("Course with Id " + id + " not found");
  }

  @Override
  public List<CourseDTO> getFilteredCourses(
      String topic, String difficultyLevel, String instructorName) {
    List<Course> courses;

    boolean hasTopic = topic != null && !topic.isBlank();
    boolean hasDifficulty = difficultyLevel != null && !difficultyLevel.isBlank();
    boolean hasInstructor = instructorName != null && !instructorName.isBlank();

    if (hasTopic && hasDifficulty && hasInstructor) {
      courses =
          courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
              topic, instructorName, difficultyLevel);
    } else if (hasTopic && hasDifficulty) {
      courses =
          courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase(
              topic, difficultyLevel);
    } else if (hasTopic && hasInstructor) {
      courses =
          courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCase(topic, instructorName);
    } else if (hasDifficulty && hasInstructor) {
      courses =
          courseRepository.findByInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
              instructorName, difficultyLevel);
    } else if (hasTopic) {
      courses = courseRepository.findByTopicIgnoreCase(topic);
    } else if (hasInstructor) {
      courses = courseRepository.findByInstructorIgnoreCase(instructorName);
    } else {
      courses = courseRepository.findAll();
    }

    return courses.stream().map(CourseDTO::new).collect(Collectors.toList());
  }

  @Override
  public Course getCourseById(int courseId) {
    return courseRepository.findById(courseId).orElse(null);
  }
}
