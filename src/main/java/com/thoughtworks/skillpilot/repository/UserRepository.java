package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Course,Integer> {
}

