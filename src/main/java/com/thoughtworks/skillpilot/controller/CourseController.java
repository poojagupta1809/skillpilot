package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.dto.CourseDTO;
import com.thoughtworks.skillpilot.exception.CourseAlreadyExistException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    @GetMapping("/admin/view")
    public ResponseEntity<?> getAllCoursesForAdmin() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }


    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<?> addCourse(@Valid @RequestBody Course course) {
        try {
            return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
        } catch (CourseAlreadyExistException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User with this ID already exists!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        courseService.removeCourseById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editCourse(@PathVariable int id, @Valid @RequestBody Course course) {
        Course updatedProduct = courseService.updateCourse(id, course);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/view")
    public List<CourseDTO> getAllCoursesForLearner() {
        return courseService.getAllCourses();
    }

    @GetMapping("/filter")
    public List<CourseDTO> getFilteredCourses(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) String instructorName
    ) {
        return courseService.getFilteredCourses(topic, difficultyLevel, instructorName);
    }
}
