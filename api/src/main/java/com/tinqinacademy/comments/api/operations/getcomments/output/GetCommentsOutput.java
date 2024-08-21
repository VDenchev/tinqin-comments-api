package com.tinqinacademy.comments.api.operations.getcomments.output;

import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.models.output.CommentDetailsOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetCommentsOutput implements OperationOutput {

  private List<CommentDetailsOutput> comments;
  private Integer pageNumber;
  private Integer totalPages;
  private Long totalElements;
  private Integer pageSize;
  private Integer numberOfElements;
  private Boolean empty;
}
