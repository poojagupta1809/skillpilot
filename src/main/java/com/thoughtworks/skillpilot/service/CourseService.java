package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.CourseDTO;
import com.thoughtworks.skillpilot.model.Course;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);

    void removeCourseById(int courseId);

    List<CourseDTO> getAllCourses();

    List<CourseDTO> getFilteredCourses(String topic, String difficultyLevel, String instructorName);

    Course getCourseById(int courseId);

    Course updateCourse(int id, Course course);
}
