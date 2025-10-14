package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.LessonNotFoundException;
import com.thoughtworks.skillpilot.exception.ProgressAlreadyExistException;
import com.thoughtworks.skillpilot.exception.ProgressNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.Lesson;
import com.thoughtworks.skillpilot.model.Progress;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.LessonRepository;
import com.thoughtworks.skillpilot.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProgressServiceImpl implements ProgressService{
    @Autowired
    private  ProgressRepository progressRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;


    @Override
    public Progress createProgress(int enrollmentId, int lessonId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found"));
        Progress exist = progressRepository.findByEnrollment_enrollmentIdAndLesson_lessonId(enrollmentId, lessonId);
        if (exist != null) {
            throw new ProgressAlreadyExistException("Progress already exist for this enrollment and lesson");
        } else {
            Progress progress = new Progress(enrollment, lesson, false);
            return progressRepository.save(progress);
        }
    }

    @Override
    public Progress markLessonComplete(int progressId) {
       Progress progress=progressRepository.findById(progressId).orElseThrow(()-> new ProgressNotFoundException("Progress not found"));
       progress.setCompleted(true);

       return progressRepository.save(progress);
    }

    @Override
    public Progress viewProgress(int progressId) {
        return progressRepository.findById(progressId).orElseThrow(()->new ProgressNotFoundException("Progress not found"));
    }

    @Override
    public List<Progress> getProgressByEnrollment(int enrollmentId) {
        return progressRepository.findByEnrollment_enrollmentId(enrollmentId);
    }

    @Override
    public List<Progress> viewProgressByLesson(int lessonId) {

        return progressRepository.findByLesson_lessonId(lessonId);

    }
}
