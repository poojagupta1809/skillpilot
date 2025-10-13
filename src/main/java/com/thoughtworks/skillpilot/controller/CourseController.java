package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    @GetMapping("/view")
    public List<Course> getAllCoursesForLearner() {
        return courseService.getAllCourses();
    }

    @GetMapping("/filter")
    public List<Course> getFilteredCourses(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) String instructorName
    ) {
        return courseService.getFilteredCourses(topic, difficultyLevel, instructorName);
    }
}
