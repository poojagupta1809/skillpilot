package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Progress;

import java.util.List;

public interface ProgressService {
    Progress createProgress(int enrollmentId,int lessonId);
    Progress markLessonComplete(int progressId);
    Progress viewProgress(int progressId);
    List<Progress>getProgressByEnrollment(int enrollmentId);
    List<Progress> viewProgressByLesson(int lessontId);
}
