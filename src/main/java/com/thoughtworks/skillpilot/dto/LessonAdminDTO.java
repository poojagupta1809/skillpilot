package com.thoughtworks.skillpilot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.skillpilot.model.Lesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.URL;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonAdminDTO {
  @NotBlank(message = "Title cannot be empty")
  @Size(max = 100)
  private String title;

  private Integer lessonId;

  @Size(max = 500, message = "Description can't exceed 500 characters")
  private String description;

  @NotBlank(message = "Content type is required")
  private String contentType; // TEXT, VIDEO, QUIZ

  private String content; // Only for TEXT lessons

  @URL(message = "Video URL must be a valid URL")
  private String videoUrl; // Only for VIDEO lessons

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer courseId;

  public LessonAdminDTO(Lesson lesson) {
    this.lessonId = lesson.getLessonId();
    this.title = lesson.getTitle();
    this.description = lesson.getDescription();
    this.contentType = lesson.getContentType().name();
    this.content = lesson.getContent();
    this.videoUrl = lesson.getVideoUrl();
    this.createdAt = lesson.getCreatedAt();
    this.updatedAt = lesson.getUpdatedAt();
    this.courseId = lesson.getCourse().getCourseId();
  }

  public LessonAdminDTO() {}

  public Integer getCourseId() {
    return courseId;
  }

  public void setCourseId(Integer courseId) {
    this.courseId = courseId;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getLessonId() {
    return lessonId;
  }

  public void setLessonId(Integer lessonId) {
    this.lessonId = lessonId;
  }
}
