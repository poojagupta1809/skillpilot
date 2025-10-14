package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Lesson;

import java.util.List;

public interface LessonService {
    public Lesson createLesson(int courseId, Lesson lesson);
    public Lesson updateLesson( int lessonId,Lesson updatedLesson);
    public List<Lesson> getLessonsByCourseId(int courseId);
}
