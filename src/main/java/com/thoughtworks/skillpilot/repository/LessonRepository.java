package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Lesson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
  List<Lesson> findByCourse_CourseId(int courseId);
}
