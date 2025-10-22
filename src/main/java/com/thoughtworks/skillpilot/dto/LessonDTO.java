package com.thoughtworks.skillpilot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.skillpilot.model.ContentType;
import com.thoughtworks.skillpilot.model.Lesson;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDTO {
    private String title;
    private String description;
    private ContentType contentType;
    private String videoUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LessonDTO() {
    }
    public LessonDTO(Lesson lesson) {
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.contentType = ContentType.valueOf(lesson.getContentType().name());
        this.content = lesson.getContent();
        this.videoUrl = lesson.getVideoUrl();
        this.createdAt = lesson.getCreatedAt();
        this.updatedAt = lesson.getUpdatedAt();
    }

    public String getTitle() {
        return title;
    }


    public ContentType getContentType() {
        return contentType;
    }


    public String getDescription() {
        return description;
    }


    public String getVideoUrl() {
        return videoUrl;
    }


    public String getContent() {
        return content;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}