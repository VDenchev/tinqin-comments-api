package com.tinqinacademy.comments.api.exceptions;

public class InvalidPageNumberException extends RuntimeException {
  public InvalidPageNumberException(String message) {
    super(message);
  }
}
