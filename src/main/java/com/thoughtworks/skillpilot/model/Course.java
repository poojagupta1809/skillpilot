package com.thoughtworks.skillpilot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "course")
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(name = "course_title")
    @NotBlank(message = "topic is required")
    @Size(min = 2, max = 100, message = "topic must be between 2 and 100 characters")
    private String topic;
    @Size(min = 2, max = 100, message = " instructor must be between 2 and 100 characters")
    private String instructor;
    @Size(min = 2, max = 255, message = "description must be between 2 and 255 characters")
    private String description;
    private String difficultyLevel;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("course-enrollment")
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("course-lesson")
    private List<Lesson> lessonList=new ArrayList<>();

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public Course() {
    }

    public Course(int courseId, String topic, String instructor, String description, String difficultyLevel) {
        this.courseId = courseId;
        this.topic = topic;
        this.instructor = instructor;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
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
