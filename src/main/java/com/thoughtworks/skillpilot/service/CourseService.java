package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    public Course createCourse(Course course) {

        return null;
    }

    public void removeCourseById(int courseId) {

    }

    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }

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

}