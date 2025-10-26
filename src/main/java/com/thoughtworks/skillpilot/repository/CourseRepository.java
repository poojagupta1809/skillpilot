package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

  List<Course> findByTopicIgnoreCase(String topic);

  List<Course> findByInstructorIgnoreCase(String instructor);

  List<Course> findByTopicIgnoreCaseAndInstructorIgnoreCase(String topic, String instructor);

  List<Course> findByTopicIgnoreCaseAndDifficultyLevelIgnoreCase(
      String topic, String difficultyLevel);

  List<Course> findByInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
      String instructor, String difficultyLevel);

  List<Course> findByTopicIgnoreCaseAndInstructorIgnoreCaseAndDifficultyLevelIgnoreCase(
      String topic, String instructor, String difficultyLevel);
}
