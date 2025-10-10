package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.service.CourseService;
import com.thoughtworks.skillpilot.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    // Need to add end points here
}
