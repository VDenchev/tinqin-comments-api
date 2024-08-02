package com.tinqinacademy.comments.api.operations.createcomment.output;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
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
public class AddCommentOutput implements OperationOutput {

  @JsonUnwrapped
  private CommentOutput output;
}
