package com.tinqinacademy.comments.api.operations.updatecomment.output;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateCommentOutput {

  @JsonUnwrapped
  private CommentOutput output;
}
