package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {
}

