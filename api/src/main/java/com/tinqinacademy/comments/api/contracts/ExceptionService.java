package com.tinqinacademy.comments.api.contracts;

import com.tinqinacademy.comments.api.models.exceptions.output.ErrorOutput;
import org.springframework.http.HttpStatusCode;

public interface ExceptionService {
  ErrorOutput processException(Exception ex, HttpStatusCode statusCode);
}
