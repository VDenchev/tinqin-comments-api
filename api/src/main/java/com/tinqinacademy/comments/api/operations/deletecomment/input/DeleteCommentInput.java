package com.tinqinacademy.comments.api.operations.deletecomment.input;

import com.fasterxml.jackson.annotation.JsonValue;
import com.tinqinacademy.comments.api.base.OperationInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteCommentInput implements OperationInput {

  @JsonValue
  private String commentId;
}
