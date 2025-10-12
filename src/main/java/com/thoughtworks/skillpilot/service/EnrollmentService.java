package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    Enrollment enrollLearnernCourse(Integer learnerId, Integer courseId);

    Optional<Enrollment> getEnrollmentById(Integer id);

    List<Enrollment> getAllEnrollments();

    void deleteEnrollment(Integer id);

    void viewProgressPercentage(Integer learnerId);

    void viewEnrollementsByCourse();
}
