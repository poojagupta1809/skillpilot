package com.thoughtworks.skillpilot.controller;

import com.thoughtworks.skillpilot.model.Progress;
import com.thoughtworks.skillpilot.service.ProgressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@RequestMapping("/api/course/lesson/progress")

public class ProgressController {
    @Autowired
    ProgressService progressService;
    @PostMapping
    public ResponseEntity<?>createProgress(@RequestBody int enrollmentId,@RequestBody int lessonId)
    {
        Progress progress=progressService.createProgress(enrollmentId,lessonId);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }
    @PutMapping("/complete/{progressId}")
    public ResponseEntity<?> completeProgress(@PathVariable int progressId)
    {
        Progress progress=progressService.markLessonComplete(progressId);
        return new ResponseEntity<>(progress, HttpStatus.OK);

    }@GetMapping("/{progressId}")
    public ResponseEntity<?> getProgress(@PathVariable int progressId) {
        Progress progress=progressService.viewProgress(progressId);
        return new ResponseEntity<>(progress,HttpStatus.OK);
    }

    // 🔹 Get all progress for a specific enrollment
    @GetMapping("/enrollment/{enrollmentId}")
    public List<Progress> getProgressByEnrollment(@PathVariable int enrollmentId) {
        return progressService.getProgressByEnrollment(enrollmentId);
    }
    @GetMapping("/lesson/{lessonId}")
    public List<Progress> getProgressByLesson(@PathVariable int lessonId) {
        return progressService.viewProgressByLesson(lessonId);
    }



}
