package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress,Integer> {
    Progress findByEnrollment_enrollmentIdAndLesson_lessonId(int enrollmentId, int lessonId);
    List<Progress> findByEnrollment_enrollmentId(int enrollmentId);
    List<Progress> findByLesson_lessonId(int lessonId);


}
