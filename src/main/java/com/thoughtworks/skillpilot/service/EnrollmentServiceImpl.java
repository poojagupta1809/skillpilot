package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.DuplicateEnrollmentException;
import com.thoughtworks.skillpilot.exception.EnrollmentNotFoundException;
import com.thoughtworks.skillpilot.exception.UserNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.model.EnrollmentStatus;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 UserRepository userRepository,
                                 CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Enrollment enrollLearnerInCourse(int userId, int courseId) {
        // Check if enrollment already exists
        Optional<Enrollment> existing = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);

        if (existing.isPresent()) {
            throw new DuplicateEnrollmentException(
                    "User " + userId + " is already enrolled in course " + courseId
            );
        }

        // Load user and course
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public boolean unenrollLearnerFromCourse(int userId, int courseId) throws EnrollmentNotFoundException {
        Enrollment enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found for userId=" + userId + ", courseId=" + courseId));
        enrollment.setStatus(EnrollmentStatus.UNENROLLED);
        enrollmentRepository.save(enrollment);
        return true;
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollmentRepository.findByCourse_CourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE);
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(int userId) {
        return enrollmentRepository.findByUser_UserIdAndStatus(userId, EnrollmentStatus.ACTIVE);
    }

    @Override
    public Optional<Enrollment> getEnrollmentById(Integer id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }


    @Override
    @Transactional
    public void deleteEnrollmentsByCourse(int courseId) {
        enrollmentRepository.deleteByCourse_CourseId(courseId);
    }

    @Override
    @Transactional
    public void deleteEnrollmentByUserAndCourse(int userId, int courseId) throws EnrollmentNotFoundException {
        Optional<Enrollment> enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        if (enrollment.isPresent()) {
            enrollmentRepository.delete(enrollment.get());
        } else {
            throw new EnrollmentNotFoundException("Enrollment not found for userId=" + userId + ", courseId=" + courseId);
        }
    }


}