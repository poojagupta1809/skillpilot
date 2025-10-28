package com.thoughtworks.skillpilot.dto;

import com.thoughtworks.skillpilot.model.Course;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDTO {
  private Integer courseId;
  private String topic;
  private String instructor;
  private String description;
  private String difficultyLevel;
  private String imageUrl;
  private String courseType;
  private String price;
  private List<LessonDTO> lessons;

  public CourseDTO() {}

  public CourseDTO(Course course) {
    this.courseId = course.getCourseId();
    this.topic = course.getTopic();
    this.instructor = course.getInstructor();
    this.description = course.getDescription();
    this.difficultyLevel = course.getDifficultyLevel();
    this.imageUrl = course.getImageUrl();
    this.courseType = course.getCourseType();
    this.price = course.getPrice();
    this.lessons = course.getLessonList().stream().map(LessonDTO::new).collect(Collectors.toList());
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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getCourseType() {
    return courseType;
  }

  public void setCourseType(String courseType) {
    this.courseType = courseType;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}
