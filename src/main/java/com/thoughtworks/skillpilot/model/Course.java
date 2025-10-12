package com.thoughtworks.skillpilot.model;

import jakarta.persistence.*;

import java.util.Set;


@Entity
@Table(name = "course")
public class Course {


    @Id
    private int courseId;

    @Column(name = "course_title")
    private String topic;
    private String instructor;


    private String description;
    private String difficultyLevel;

    public Course() {
    }

    public Course(int courseId, String topic, String instructor, String description, String difficultyLevel) {
        this.courseId = courseId;
        this.topic = topic;
        this.instructor = instructor;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }


    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }



}
