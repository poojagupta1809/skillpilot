package com.thoughtworks.skillpilot.exception;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(String message) {
        super(message);
    }
}