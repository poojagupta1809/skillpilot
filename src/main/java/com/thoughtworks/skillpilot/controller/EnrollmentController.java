package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.DTO.EnrollmentDTO;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    // Enroll user in a course
    // Endpoint: POST /api/enrollments/{courseId}/enroll?userId=1

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enroll(@PathVariable int courseId, @RequestParam int userId) {
        try {

            Enrollment enrollment = enrollmentService.enrollLearnerInCourse(userId, courseId);


            EnrollmentDTO dto = convertToDTO(enrollment);


            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (DuplicateEnrollmentException ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | CourseNotFoundException ex) {
            // If user or course doesn't exist
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Unenroll a user from a course
    // Endpoint: DELETE /api/enrollments/{courseId}/unenroll?userId=1

    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<?> unenroll(@PathVariable int courseId, @RequestParam int userId) {
        try {
            // Change enrollment status to UNENROLLED
            enrollmentService.unenrollLearnerFromCourse(userId, courseId);

            // Return confirmation message
            return new ResponseEntity<>("Enrollment updated to UNENROLLED", HttpStatus.OK);
        } catch (EnrollmentNotFoundException ex) {
            // If the enrollment doesn't exist
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // get a single enrollment by its ID
    // Endpoint: GET /api/enrollments/{id}

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable int id) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);

        if (enrollment.isPresent()) {
            return new ResponseEntity<>(convertToDTO(enrollment.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Enrollment not found", HttpStatus.NOT_FOUND);
        }
    }


    // get all enrollments (admin use)
    // Endpoint: GET /api/enrollments/getAllEnrollments

    @GetMapping("/getAllEnrollments")
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        // get all enrollments and convert them to DTO list
        List<EnrollmentDTO> dtos = enrollmentService.getAllEnrollments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // return list of all enrollments
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    // Get all enrollments for a course
    // Endpoint: GET /api/enrollments/course/{courseId}

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable int courseId) {
        List<EnrollmentDTO> dtos = enrollmentService.getEnrollmentsByCourse(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    // get all enrollments for a user
    // Endpoint: GET /api/enrollments/user/{userId}

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUser(@PathVariable int userId) {
        List<EnrollmentDTO> dtos = enrollmentService.getEnrollmentsByUser(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    // delete all enrollments for a course
    // Endpoint: DELETE /api/enrollments/course/{courseId}

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<String> deleteEnrollmentsByCourse(@PathVariable int courseId) {

        enrollmentService.deleteEnrollmentsByCourse(courseId);
        return new ResponseEntity<>("All enrollments for course " + courseId + " deleted", HttpStatus.OK);
    }


    // delete one specific enrollment (by user and course)
    // Endpoint: DELETE /api/enrollments/course/{courseId}/user/{userId}

    @DeleteMapping("/course/{courseId}/user/{userId}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable int courseId, @PathVariable int userId) {
        try {
            enrollmentService.deleteEnrollmentByUserAndCourse(userId, courseId);
            return new ResponseEntity<>("Enrollment deleted successfully", HttpStatus.OK);
        } catch (EnrollmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // method to convert an Enrollment entity to EnrollmentDTO
    // using this to send only necessary fields to the client

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getEnrollmentId(),                          // iD of the enrollment
                enrollment.getUser().getUserId(),                     // iD of the user
                enrollment.getUser().getUsername(),                       // name of the user
                enrollment.getCourse().getCourseId(),                 // iD of the course
                enrollment.getCourse().getTopic(),                    // title of the course
                enrollment.getStatus(),                               // ACTIVE or UNENROLLED
                enrollment.getEnrollmentDate()                        // date of enrollment
        );
    }
}
