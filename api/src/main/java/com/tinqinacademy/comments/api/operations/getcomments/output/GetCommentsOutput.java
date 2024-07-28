package com.tinqinacademy.comments.api.operations.getcomments.output;

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
public class GetCommentsOutput {

  private List<CommentDetailsOutput> comments;
}
