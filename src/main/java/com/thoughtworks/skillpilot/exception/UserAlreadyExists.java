package com.thoughtworks.skillpilot.exception;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists(String message) {
        super(message);

    }
}
