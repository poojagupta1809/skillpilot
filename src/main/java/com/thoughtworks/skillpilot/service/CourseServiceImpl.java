package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseAlreadyExistException;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Course createCourse(Course course) {
        if (course.getCourseId() != null && courseRepository.existsById(course.getCourseId())) {
            throw new CourseAlreadyExistException("Course with this ID already exists!");
        }
        course.setCourseId(null);
        try {
            courseRepository.save(course);
            return course;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }
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
    public List<Course> getAllCourses() {

        return courseRepository.findAll();
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
            try {
                courseRepository.save(courseToUpdate);
                return courseToUpdate;
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                return null;
            }
        } else
            throw new CourseNotFoundException("Course with Id " + id + " not found");
    }

    @Override
    public List<Course> getFilteredCourses(String topic, String difficultyLevel, String instructorName) {

        boolean hasTopic = topic != null && !topic.isEmpty();
        boolean hasDifficulty = difficultyLevel != null && !difficultyLevel.isEmpty();
        boolean hasInstructor = instructorName != null && !instructorName.isEmpty();

        if (hasTopic && hasDifficulty && hasInstructor) {
            return courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(topic, instructorName, difficultyLevel);
        } else if (hasTopic && hasDifficulty) {
            return courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase(topic, difficultyLevel);
        } else if (hasTopic && hasInstructor) {
            return courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCase(topic, instructorName);
        } else if (hasDifficulty && hasInstructor) {
            return courseRepository.findByInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(instructorName, difficultyLevel);
        } else if (hasTopic) {
            return courseRepository.findByTopicIgnoreCase(topic);
        } else if (hasInstructor) {
            return courseRepository.findByInstructorIgnoreCase(instructorName);
        } else {
            return courseRepository.findAll();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
}