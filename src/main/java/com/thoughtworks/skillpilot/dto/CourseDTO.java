package com.thoughtworks.skillpilot.dto;

import java.util.List;

public class CourseDTO {
    private String topic;
    private String instructor;
    private String description;
    private String difficultyLevel;
    private List<LessonDTO> lessons;

    public CourseDTO() {
    }

    public CourseDTO(String topic, String instructor, String description, String difficultyLevel, List<LessonDTO> lessons) {
        this.topic = topic;
        this.instructor = instructor;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.lessons = lessons;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}