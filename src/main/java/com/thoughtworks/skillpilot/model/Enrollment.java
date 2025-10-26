package com.thoughtworks.skillpilot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "enrollment",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer enrollmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonBackReference
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  @JsonBackReference("course-enrollment")
  private Course course;

  private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

  private LocalDateTime enrollmentDate = LocalDateTime.now();
  private LocalDateTime completionDate;

  // Constructors
  public Enrollment() {}

  public Enrollment(User user, Course course) {
    this.user = user;
    this.course = course;
  }

  // Getters and Setters

  public Integer getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(Integer enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public EnrollmentStatus getStatus() {
    return status;
  }

  public void setStatus(EnrollmentStatus status) {
    this.status = status;
  }

  public LocalDateTime getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(LocalDateTime completionDate) {
    this.completionDate = completionDate;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public LocalDateTime getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(LocalDateTime enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }
}
