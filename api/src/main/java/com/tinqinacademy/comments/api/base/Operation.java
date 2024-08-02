package com.tinqinacademy.comments.api.base;

public interface Operation<I extends OperationInput, O extends OperationOutput> {
  O process(I input);
}
