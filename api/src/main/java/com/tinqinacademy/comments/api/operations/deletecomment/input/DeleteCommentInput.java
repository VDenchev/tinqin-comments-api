package com.tinqinacademy.comments.api.operations.deletecomment.input;

import com.tinqinacademy.comments.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteCommentInput implements OperationInput {

  @UUID(message = "Comment id has to be a valid UUID string")
  @NotBlank(message = "Comment id cannot be blank")
  private String commentId;
}
