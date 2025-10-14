package com.thoughtworks.skillpilot.exception;

public class CourseAlreadyExistException extends RuntimeException{
    public CourseAlreadyExistException(String message){
        super(message);
    }
}
