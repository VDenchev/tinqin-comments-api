package com.tinqinacademy.comments.rest.exceptions;

import com.tinqinacademy.comments.api.contracts.ExceptionService;
import com.tinqinacademy.comments.api.models.exceptions.output.ErrorOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ExceptionService exceptionService;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorOutput> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex
  ) {
    final HttpStatus HTTP_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    ErrorOutput output = exceptionService.processException(ex, HTTP_STATUS);

    return new ResponseEntity<>(output,HTTP_STATUS);
  }
}
