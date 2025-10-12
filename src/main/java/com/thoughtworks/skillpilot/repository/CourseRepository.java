package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

   List<Course> findByTopicIgnoreCase(String topic);

   List<Course> findByInstructorIgnoreCase(String instructor);

   List<Course> findByTopicIgnoreCaseAndInstructorIgnoreCase(String topic, String instructor);

   List<Course> findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase(String topic, String difficultyLevel);

   List<Course> findByInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(String instructor, String difficultyLevel);

   List<Course> findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(String topic, String instructor, String difficultyLevel);

}
