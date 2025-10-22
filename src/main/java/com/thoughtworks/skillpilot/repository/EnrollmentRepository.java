package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.EnrollmentStatus;
import com.thoughtworks.skillpilot.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // Find single enrollment for user in a course
    Optional<Enrollment> findByUser_UserIdAndCourse_CourseId(int userId, int courseId);

    // All enrollments in a course with status
    List<Enrollment> findByCourse_CourseIdAndStatus(int courseId, EnrollmentStatus status);

    // All enrollments for a user with status
    List<Enrollment> findByUser_UserIdAndStatus(int userId, EnrollmentStatus status);

    // delete all enrollments for a course
    @Modifying
    @Transactional
    void deleteByCourse_CourseId(int courseId);
}