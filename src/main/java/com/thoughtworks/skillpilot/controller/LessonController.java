package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;

    }

    @PostMapping("/add/{courseId}")
    public ResponseEntity<?> createLesson(@PathVariable int courseId, @Valid @RequestBody Lesson lesson) {
        return new ResponseEntity<>(lessonService.createLesson(courseId, lesson), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLesson(@PathVariable int lessonId,@Valid @RequestBody Lesson lesson) {
        return new ResponseEntity<>(lessonService.updateLesson(lessonId, lesson), HttpStatus.OK);
    }
    @GetMapping("/view/{courseId}")
    public ResponseEntity<?> getLessonsByCourse(@PathVariable int courseId) {
        return new ResponseEntity<>(lessonService.getLessonsByCourseId(courseId),HttpStatus.OK);
    }
}
