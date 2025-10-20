package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.service.EnrollmentService;
import com.thoughtworks.skillpilot.service.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Enroll a learner
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enroll(@PathVariable int courseId, @RequestParam int userId) {
        try {
            Enrollment enrollment = enrollmentService.enrollLearnerInCourse(userId, courseId);
            return new ResponseEntity<>(enrollment, HttpStatus.CREATED);

        } catch (DuplicateEnrollmentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | CourseNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Unenroll a learner
    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<String> unenroll(@PathVariable int courseId, @RequestParam int userId) {
        enrollmentService.unenrollLearnerFromCourse(userId, courseId);
        return new ResponseEntity<>("Enrollment deleted successfully", HttpStatus.OK);
    }

    // Get enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable int id) {
        return new ResponseEntity<>(enrollmentService.getEnrollmentById(id), HttpStatus.OK);
    }

    // Get all enrollments
    @GetMapping("/getAllEnrollments")
    public ResponseEntity<?> getAllEnrollments() {
        return new ResponseEntity<>(enrollmentService.getAllEnrollments(), HttpStatus.OK);
    }

    // Get all enrollments for a course (Admin)
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourse(@PathVariable int courseId) {
        return new ResponseEntity<>(enrollmentService.getEnrollmentsByCourse(courseId), HttpStatus.OK);
    }

    // Get all enrollments for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEnrollmentsByUser(@PathVariable int userId) {
        return new ResponseEntity<>(enrollmentService.getEnrollmentsByUser(userId), HttpStatus.OK);
    }

    // Admin deletes all enrollments of a course
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<String> deleteEnrollmentsByCourse(@PathVariable int courseId) {
        enrollmentService.deleteEnrollmentsByCourse(courseId);
        return new ResponseEntity<>("All enrollments for course " + courseId + " deleted", HttpStatus.OK);
    }

    // Admin deletes a specific enrollment
    @DeleteMapping("/course/{courseId}/user/{userId}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable int courseId, @PathVariable int userId) {
        enrollmentService.deleteEnrollmentByUserAndCourse(userId, courseId);
        return new ResponseEntity<>("Enrollment deleted successfully", HttpStatus.OK);
    }
}