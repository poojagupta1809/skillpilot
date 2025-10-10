package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }

    // Endpoint to create a new enrollment

    // Endpoint to retrieve a single enrollment by its ID

    // Endpoint to retrieve all enrollments


    // Endpoint to delete an enrollment

}