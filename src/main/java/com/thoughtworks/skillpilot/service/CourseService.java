package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.exception.CourseNotFoundException;
import com.thoughtworks.skillpilot.exception.UserAlreadyExistException;
import com.thoughtworks.skillpilot.model.Course;
import com.thoughtworks.skillpilot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Course Service
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    public Course createCourse(Course course) {
        if ( course.getCourseId()!=null &&  courseRepository.existsById(course.getCourseId())) {
            throw new UserAlreadyExistException("User with this ID already exists!");
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

    public void removeCourseById(int courseId) {
        Optional<Course>  isCourseExist = courseRepository.findById(courseId);
        if(isCourseExist.isPresent())
            courseRepository.deleteById(courseId);
        else
            throw new CourseNotFoundException("Course with Id "+courseId+ " not found");
    }

    public List<Course> getAllCourses() {
        List<Course> allCourses =  courseRepository.findAll();
        return allCourses;
    }

    public List<Course> getFilteredCourses(String topic, String difficultyLevel,String instructorName) {

        return null;
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
}