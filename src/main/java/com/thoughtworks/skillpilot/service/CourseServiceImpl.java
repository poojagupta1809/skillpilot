package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Course Service
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Course createCourse(Course course) {

        return null;
    }

    @Override
    public void removeCourseById(int courseId) {

    }

    @Override
    public List<Course> getAllCourses() {

        return null;
    }

    @Override
    public List<Course> getFilteredCourses(String topic, String difficultyLevel, String instructorName) {

        return null;
    }

    @Override
    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
}