package com.tinqinacademy.comments.api.base;

import com.tinqinacademy.comments.api.errors.ErrorOutput;
import io.vavr.control.Either;

public interface Operation<I extends OperationInput, O extends OperationOutput> {
  Either<ErrorOutput,O> process(I input);
}
