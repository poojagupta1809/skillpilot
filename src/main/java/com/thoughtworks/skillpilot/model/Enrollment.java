package com.thoughtworks.skillpilot.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


    // OneToMany relationship with Progress
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Progress> progressRecords = new HashSet<>();


    private LocalDateTime enrollmentDate = LocalDateTime.now();

    // can do this way later for now putting a string type only,
//    @Enumerated(EnumType.STRING)
//    private CompletionStatus completionStatus = CompletionStatus.IN_PROGRESS;


    private String completionStatus;

    // Constructors
    public Enrollment() {
    }

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStudent(User user) {
        this.user = user;
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

    public Set<Progress> getProgressRecords() {
        return progressRecords;
    }

    public void setProgressRecords(Set<Progress> progressRecords) {
        this.progressRecords = progressRecords;
    }


    //public CompletionStatus getCompletionStatus() { return completionStatus; }
    //public void setCompletionStatus(CompletionStatus completionStatus) { this.completionStatus = completionStatus; }
}

//enum CompletionStatus {
//    IN_PROGRESS,
//    COMPLETED
//}