package com.thoughtworks.skillpilot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "lesson")
public class Lesson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int lessonId;

  @NotBlank(message = "Title cannot be empty")
  @Size(max = 100)
  private String title;

  @Size(max = 500, message = "Description can't exceed 500 characters")
  @Column(length = 500)
  private String description; // short summary about the lesson

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ContentType contentType; // VIDEO, TEXT, QUIZ

  @Lob private String content; // for text based lesson

  @URL(message = "Video URL must be a valid URL")
  private String videoUrl;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  @JsonBackReference("course-lesson")
  private Course course;

  public Lesson() {}

  public Lesson(
      String title,
      String description,
      ContentType contentType,
      String content,
      String videoUrl,
      Course course) {
    this.title = title;
    this.description = description;
    this.contentType = contentType;
    this.content = content;
    this.videoUrl = videoUrl;
    this.course = course;
  }

  public int getLessonId() {
    return lessonId;
  }

  public void setLessonId(int lessonId) {
    this.lessonId = lessonId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ContentType getContentType() {
    return contentType;
  }

  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
