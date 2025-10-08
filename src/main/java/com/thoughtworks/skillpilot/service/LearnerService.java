package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Learner Service
@Service
public class LearnerService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;



    public void enrollInCourse(int learnerId, int courseId) {
    }

    public void unEnrollInCourse(int learnerId, int courseId) {
    }

}