package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.Enrollment;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.EnrollmentRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Enrollment enrollLearnernCourse(Integer learnerId, Integer courseId) {
        return null;
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
    public void deleteEnrollment(Integer id) {

    }

    @Override
    public void viewProgressPercentage(Integer learnerId)
    {

    }

    @Override
    public void viewEnrollementsByCourse()
    {

    }
}