package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.LessonAdminDTO;
import com.thoughtworks.skillpilot.dto.LessonDTO;
import com.thoughtworks.skillpilot.model.Lesson;

import java.util.List;

public interface LessonService {
  LessonAdminDTO createLesson(int courseId, LessonAdminDTO lessonDTO);

  LessonAdminDTO updateLesson(int lessonId, LessonAdminDTO lessonDTO);

  void deleteLessonById(int lessonId);

  List<LessonAdminDTO> getLessonsByCourseId(int courseId);

  List<LessonDTO> getCourseLessonsForLearners(int courseId);
Lesson getLessonById(int lessonid);
}
