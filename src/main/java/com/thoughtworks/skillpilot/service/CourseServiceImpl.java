package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseAlreadyExistException;
import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Course createCourse(Course course) {
        if ( course.getCourseId()!=null &&  courseRepository.existsById(course.getCourseId())) {
            throw new CourseAlreadyExistException("Course with this ID already exists!");
        }

        course.setCourseId(null);
        try {
            courseRepository.save(course); //request keep g
            return course;
        }
        catch(RuntimeException e) {
            System.out.println(e.getMessage());
            return  null;
        }

    }

    @Override
    public void removeCourseById(int courseId) {
        Optional<Course> isCourseExist = courseRepository.findById(courseId);
        if(isCourseExist.isPresent())
            courseRepository.deleteById(courseId);
        else
            throw new CourseNotFoundException("Course with Id "+courseId+ " not found");
    }


    @Override
    public List<Course> getAllCourses() {

        return courseRepository.findAll();
    }

    public Course updateCourse(int id, Course course) {

        Optional<Course> existingCourse = courseRepository.findById(id);

        if (existingCourse.isPresent()) {
            if (course.getDescription() != null) {
                existingCourse.get().setDescription( course.getDescription());
            }
            if (course.getTopic() != null) {
                existingCourse.get().setTopic( course.getTopic());
            }
            if (course.getDifficultyLevel() != null) {
                existingCourse.get().setDifficultyLevel( course.getDifficultyLevel());
            }
            if (course.getInstructor() != null) {
                existingCourse.get().setInstructor( course.getInstructor());
            }
            try {
                courseRepository.save(existingCourse.get());
                return existingCourse.get();
            }catch (RuntimeException e){
                System.out.println(e.getMessage());
                return null;
            }
        }
        else
            throw new CourseNotFoundException("Course with Id "+id+" not found");
    }

@Override
    public List<Course> getFilteredCourses(String topic, String difficultyLevel, String instructorName) {

        boolean hasTopic = topic != null && !topic.isEmpty();
        boolean hasDifficulty = difficultyLevel != null && !difficultyLevel.isEmpty();
        boolean hasInstructor = instructorName != null && !instructorName.isEmpty();

        if (hasTopic && hasDifficulty && hasInstructor) {
            return courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(topic, instructorName, difficultyLevel);
        } else if (hasTopic && hasDifficulty) {
            return courseRepository.findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase(topic, difficultyLevel);
        } else if (hasTopic && hasInstructor) {
            return courseRepository.findByTopicIgnoreCaseAndInstructorIgnoreCase(topic, instructorName);
        } else if (hasDifficulty && hasInstructor) {
            return courseRepository.findByInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(instructorName, difficultyLevel);
        } else if (hasTopic) {
            return courseRepository.findByTopicIgnoreCase(topic);
        } else if (hasInstructor) {
            return courseRepository.findByInstructorIgnoreCase(instructorName);
        } else {
            return courseRepository.findAll();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
}