package com.tinqinacademy.comments.rest.base;

import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.base.Output;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

  protected ResponseEntity<Output> createResponse(
      Either<? extends ErrorOutput, ? extends OperationOutput> source,
      HttpStatus statusCode
  ) {
    return source
        .fold(
            errorOutput -> new ResponseEntity<>(errorOutput, errorOutput.getStatusCode()),
            output -> new ResponseEntity<>(output, statusCode)
        );
  }
}
