package com.thoughtworks.skillpilot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime enrollmentDate = LocalDateTime.now();

//    @Enumerated(EnumType.STRING)
//    private CompletionStatus completionStatus = CompletionStatus.IN_PROGRESS;

    // Constructors
    public Enrollment() {}

    public Enrollment( User user, Course course) {
        this.user = user;
        this.course = course;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setStudent(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    //public CompletionStatus getCompletionStatus() { return completionStatus; }
    //public void setCompletionStatus(CompletionStatus completionStatus) { this.completionStatus = completionStatus; }
}

//enum CompletionStatus {
//    IN_PROGRESS,
//    COMPLETED
//}