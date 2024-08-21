package com.tinqinacademy.comments.api.operations.updatecomment.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.comments.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Builder(toBuilder = true)
@ToString
public class UpdateCommentInput implements OperationInput {

  @JsonIgnore
  @UUID(message = "Comment id has to be a valid UUID string")
  @NotBlank(message = "Commend id cannot be blank")
  private String commentId;

  @UUID(message = "User id has to be a valid UUID string")
  @NotBlank(message = "User id cannot be blank")
  private String userId;

  @NotBlank(message = "Content cannot be blank")
  @Size(max = 1_000, message = "Content must be a maximum of 1000 character long")
  @Schema(example = "I changed my mind. This hotel is trash")
  private String content;
}
