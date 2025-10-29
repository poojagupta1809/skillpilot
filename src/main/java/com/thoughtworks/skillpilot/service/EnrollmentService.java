package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentService {

  public Enrollment enrollLearnerInCourse(int userId, int courseId);

  public boolean unenrollLearnerFromCourse(int userId, int courseId)
      throws EnrollmentNotFoundException;

  public List<Enrollment> getEnrollmentsByCourse(int courseId);

  public List<Enrollment> getEnrollmentsByUser(int userId);

  public Optional<Enrollment> getEnrollmentById(Integer id);

  public List<Enrollment> getAllEnrollments();

  public void deleteEnrollmentsByCourse(int courseId); // Admin deletes all enrollments for a course

  public void deleteEnrollmentByUserAndCourse(int userId, int courseId)
      throws EnrollmentNotFoundException; // Admin removes a specific learner

  public Integer calculateCompletionPercentage(int userId, int courseId, Integer completedLessons);

  public Integer updateLessonCompleted(int userId, int courseId, Integer completedLessons);
}
