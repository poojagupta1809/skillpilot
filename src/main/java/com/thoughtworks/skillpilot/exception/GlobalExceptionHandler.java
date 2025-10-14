package com.thoughtworks.skillpilot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> CourseNotFoundException(CourseNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLessonNotFoundException(LessonNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EnrollmentNotFoundException.class)
    public ResponseEntity<String> handleEnrollmentNotFound(EnrollmentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEnrollmentException.class)
    public ResponseEntity<String> handleDuplicateEnrollment(DuplicateEnrollmentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception e) {
        return new ResponseEntity<>("Something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
