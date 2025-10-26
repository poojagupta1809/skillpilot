package com.thoughtworks.skillpilot.dto;

import com.thoughtworks.skillpilot.model.EnrollmentStatus;

import java.time.LocalDateTime;

public class EnrollmentDTO {
    private int enrollmentId;
    private int userId;
    private String userName;
    private int courseId;
    private String courseTitle;
    private EnrollmentStatus status;
    private LocalDateTime enrollmentDate;


    public EnrollmentDTO(int enrollmentId, int userId, String userName, int courseId, String courseTitle, EnrollmentStatus status, LocalDateTime enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.userName = userName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
    }


    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}