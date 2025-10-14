package com.thoughtworks.skillpilot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ManyToOne relationship with Enrollment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    private int completionPercentage;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;


    public Progress(boolean isCompleted, int completionPercentage, Enrollment enrollment) {
        this.isCompleted = isCompleted;
        this.completionPercentage = completionPercentage;
        this.enrollment = enrollment;
    }

    // Default constructor required by JPA
    protected Progress() {

    }

    public Integer getId() {
        return id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}