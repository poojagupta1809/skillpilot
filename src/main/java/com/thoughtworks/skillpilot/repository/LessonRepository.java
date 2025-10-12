package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    List<Lesson> findByCourse_CourseId(int courseId);

}
