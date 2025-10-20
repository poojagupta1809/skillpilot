package com.thoughtworks.skillpilot.exception;

public class DuplicateEnrollmentException extends RuntimeException{
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}