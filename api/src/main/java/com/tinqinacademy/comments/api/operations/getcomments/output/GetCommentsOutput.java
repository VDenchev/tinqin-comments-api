package com.tinqinacademy.comments.api.operations.getcomments.output;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.models.output.CommentDetailsOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetCommentsOutput implements OperationOutput {

  @JsonUnwrapped
  private Page<CommentDetailsOutput> page;
}
