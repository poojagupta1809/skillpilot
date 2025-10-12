package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Course Service
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

        return null;
    }

    public List<Course> getFilteredCourses(String topic, String difficultyLevel,String instructorName) {
        return null;
    }


}