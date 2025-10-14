package com.thoughtworks.skillpilot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonId;

    @Column(nullable = false)
    @NotBlank(message = "Lesson title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    private String content;


    private String videoUrl; // URL for video content, if applicable

    @ManyToOne(fetch = FetchType.LAZY) // Many lessons can belong to one course
    @JoinColumn(name = "course_id", nullable = false) // Foreign key to Course entity
    @JsonBackReference
    private Course course;

    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progress> progressList;

    // Constructors
    public Lesson() {
    }


    // Getters and Setters


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + lessonId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", course=" + course +
                '}';
    }

    public List<Progress> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<Progress> progressList) {
        this.progressList = progressList;
    }

}