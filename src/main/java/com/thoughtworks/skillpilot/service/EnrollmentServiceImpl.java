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


    // Enroll a user in a course

    @Override
    public Enrollment enrollLearnerInCourse(int userId, int courseId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }

        Optional<Enrollment> existing = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        if (existing.isPresent()) {
            throw new DuplicateEnrollmentException("User " + userId + " is already enrolled in course " + courseId);
        }

        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Enrollment enrollment = new Enrollment(user, course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }


    // Unenroll a user from a course

    @Override
    public boolean unenrollLearnerFromCourse(int userId, int courseId) {
        Enrollment enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found for userId=" + userId + ", courseId=" + courseId));

        enrollment.setStatus(EnrollmentStatus.UNENROLLED);
        enrollmentRepository.save(enrollment);
        return true;
    }


    // Get all active enrollments in a course

    @Override
    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }

        return enrollmentRepository.findByCourse_CourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE);
    }


    // Get all active enrollments for a user

    @Override
    public List<Enrollment> getEnrollmentsByUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        return enrollmentRepository.findByUser_UserIdAndStatus(userId, EnrollmentStatus.ACTIVE);
    }


    // Get an enrollment by ID

    @Override
    public Optional<Enrollment> getEnrollmentById(Integer id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException("Enroll not found with id: " + id);
        }

        return enrollmentRepository.findById(id);

    }


    // Get all enrollments in the system

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }


    // Admin deletes all enrollments for a course

    @Override
    @Transactional
    public void deleteEnrollmentsByCourse(int courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }

        enrollmentRepository.deleteByCourse_CourseId(courseId);
    }


    // Admin deletes specific enrollment of a user in a course

    @Override
    @Transactional
    public void deleteEnrollmentByUserAndCourse(int userId, int courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);

        if (enrollment.isEmpty()) {
            throw new EnrollmentNotFoundException("Enrollment not found for userId=" + userId + ", courseId=" + courseId);
        }

        enrollmentRepository.delete(enrollment.get());
    }
}
