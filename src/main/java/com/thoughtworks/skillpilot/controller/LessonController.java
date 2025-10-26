package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.dto.LessonAdminDTO;
import com.thoughtworks.skillpilot.service.LessonServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class LessonController {
  private final LessonServiceImpl lessonService;

  public LessonController(LessonServiceImpl lessonService) {
    this.lessonService = lessonService;
  }

  @PostMapping("/{courseId}/lessons")
  public ResponseEntity<LessonAdminDTO> createLesson(
      @PathVariable int courseId, @Valid @RequestBody LessonAdminDTO lesson) {
    return new ResponseEntity<>(lessonService.createLesson(courseId, lesson), HttpStatus.CREATED);
  }

  @PutMapping("/lessons/{lessonId}")
  public ResponseEntity<LessonAdminDTO> updateLesson(
      @PathVariable int lessonId, @Valid @RequestBody LessonAdminDTO lesson) {
    return new ResponseEntity<>(lessonService.updateLesson(lessonId, lesson), HttpStatus.OK);
  }

  @GetMapping("{courseId}/lessons")
  public ResponseEntity<?> getLessonsByCourse(@PathVariable int courseId) {
    return new ResponseEntity<>(lessonService.getLessonsByCourseId(courseId), HttpStatus.OK);
  }

  @DeleteMapping("/lessons/{lessonId}")
  public ResponseEntity<?> deleteLesson(@PathVariable int lessonId) {
    lessonService.deleteLessonById(lessonId);
    return new ResponseEntity<>("Lesson deleted successfully", HttpStatus.OK);
  }

  @GetMapping("/{courseId}/lessons/learner")
  public ResponseEntity<?> getLessonsForCourseByLearner(@PathVariable int courseId) {
    return new ResponseEntity<>(lessonService.getCourseLessonsForLearners(courseId), HttpStatus.OK);
  }
}
