package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.dto.EnrollmentDTO;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.service.EnrollmentService;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @Autowired
  public EnrollmentController(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  // Enroll user in a course
  // Endpoint: POST courses/{courseId}/enrollments/{userId}
  @PostMapping("/courses/{courseId}/enrollments/{userId}")
  public ResponseEntity<?> enroll(@PathVariable int courseId, @PathVariable int userId) {
    try {
      Enrollment enrollment = enrollmentService.enrollLearnerInCourse(userId, courseId);
      EnrollmentDTO dto = convertToDTO(enrollment);
      return new ResponseEntity<>(dto, HttpStatus.CREATED);
    } catch (DuplicateEnrollmentException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (UserNotFoundException | CourseNotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // Unenroll a user from a course
  // Endpoint: DELETE /api/courses/{courseId}/enrollments/{userId}
  @DeleteMapping("/courses/{courseId}/enrollments/{userId}")
  public ResponseEntity<?> unenroll(@PathVariable int courseId, @PathVariable int userId) {
    try {
      enrollmentService.unenrollLearnerFromCourse(userId, courseId);
      return new ResponseEntity<>("Enrollment updated to UNENROLLED", HttpStatus.OK);
    } catch (EnrollmentNotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

    @PatchMapping("/courses/{courseId}/completeEnrollment/{userId}")
    public ResponseEntity<?> enrollmentComplete(@PathVariable int courseId, @PathVariable int userId) {
        try {
         Enrollment completedEnrollment =  enrollmentService.enrollmentCompleted(userId, courseId);
            return new ResponseEntity<>(completedEnrollment, HttpStatus.OK);
        } catch (EnrollmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

  // get a single enrollment by its ID
  // Endpoint: GET enrollments/{enrollmentId}
  @GetMapping("/{enrollmentId}")
  public ResponseEntity<?> getEnrollmentById(@PathVariable int enrollmentId) {
    Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(enrollmentId);
    if (enrollment.isPresent()) {
      return new ResponseEntity<>(convertToDTO(enrollment.get()), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Enrollment not found", HttpStatus.NOT_FOUND);
    }
  }

  // get all enrollments (admin use)
  // Endpoint: GET /enrollments
  @GetMapping
  public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
    List<EnrollmentDTO> dtos =
        enrollmentService.getAllEnrollments().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }

  // Get all enrollments for a course
  // Endpoint: GET /courses/{courseId}/enrollments
  @GetMapping("/courses/{courseId}/enrollments")
  public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable int courseId) {
    List<EnrollmentDTO> dtos =
        enrollmentService.getEnrollmentsByCourse(courseId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }

  // get all enrollments for a user
  // Endpoint: GET /users/{userId}/enrollments
  @GetMapping("/users/{userId}/enrollments")
  public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUser(@PathVariable int userId) {
    List<EnrollmentDTO> dtos =
        enrollmentService.getEnrollmentsByUser(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }

  // delete all enrollments for a course
  // Endpoint: DELETE courses/{courseId}/enrollments
  @DeleteMapping("/courses/{courseId}/enrollments")
  public ResponseEntity<String> deleteEnrollmentsByCourse(@PathVariable int courseId) {
    enrollmentService.deleteEnrollmentsByCourse(courseId);
    return new ResponseEntity<>(
        "All enrollments for course " + courseId + " deleted", HttpStatus.OK);
  }

  // delete one specific enrollment (by user and course)
  // Endpoint: DELETE /courses/{courseId}/enrollments/{userId}
  @DeleteMapping("/courses/{courseId}/enrollments/{userId}/delete")
  public ResponseEntity<?> deleteEnrollment(@PathVariable int courseId, @PathVariable int userId) {
    try {
      enrollmentService.deleteEnrollmentByUserAndCourse(userId, courseId);
      return new ResponseEntity<>("Enrollment deleted successfully", HttpStatus.OK);
    } catch (EnrollmentNotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/courses/{courseId}/user/{userId}/updateProgress")
  public ResponseEntity<?> updateProgress(
      @PathVariable int userId, @PathVariable int courseId, @RequestParam int completedLessons) {

    Integer percentage =
        enrollmentService.updateLessonCompleted(userId, courseId, completedLessons);
    if (percentage != null) {
      HashMap<String, Object> responseMap = new HashMap<>();
      responseMap.put("progressPercentage", percentage);
      return new ResponseEntity<>(responseMap, HttpStatus.OK);
    } else {
      String msg = "Enrollment not found for userId=" + userId + ", courseId=" + courseId;
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
  }

  private EnrollmentDTO convertToDTO(Enrollment enrollment) {
    EnrollmentDTO responseDto =
        new EnrollmentDTO(
            enrollment.getEnrollmentId(),
            enrollment.getUser().getUserId(),
            enrollment.getUser().getUsername(),
            enrollment.getCourse().getCourseId(),
            enrollment.getCourse().getTopic(),
            enrollment.getStatus(),
            enrollment.getEnrollmentDate());

    int percentage =
        enrollmentService.calculateCompletionPercentage(
            enrollment.getUser().getUserId(),
            enrollment.getCourse().getCourseId(),
            enrollment.getCompletedLessonsCount());
    responseDto.setProgressPercentage(percentage);

    return responseDto;
  }
}
