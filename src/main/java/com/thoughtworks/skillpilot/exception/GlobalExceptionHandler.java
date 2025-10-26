package com.thoughtworks.skillpilot.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String message = error.getDefaultMessage();
              errors.put(fieldName, message);
            });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
      UserAlreadyExistsException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InvalidRoleException.class)
  public ResponseEntity<Map<String, String>> handleInvalidRoleException(InvalidRoleException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlecourseNotFoundException(
      CourseNotFoundException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CourseAlreadyExistException.class)
  public ResponseEntity<Map<String, String>> handlecourseAlreadyExistException(
      CourseAlreadyExistException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(LessonNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleLessonNotFoundException(
      LessonNotFoundException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EnrollmentNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEnrollmentNotFound(
      EnrollmentNotFoundException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicateEnrollmentException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateEnrollment(
      DuplicateEnrollmentException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneric(Exception e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Something went wrong: " + e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
