package com.thoughtworks.skillpilot.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
