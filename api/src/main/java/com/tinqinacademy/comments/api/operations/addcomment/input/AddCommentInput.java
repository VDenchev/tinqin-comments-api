package com.tinqinacademy.comments.api.operations.addcomment.input;

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
@Builder
@ToString
public class AddCommentInput implements OperationInput {

  @JsonIgnore
  @UUID(message = "Room id has to be a valid UUID string")
  @NotBlank(message = "Room id cannot be blank")
  private String roomId;

  @UUID(message = "User id has to be a valid UUID string")
  @NotBlank(message = "User id cannot be blank")
  private String userId;

  @NotBlank(message = "First name cannot be blank")
  @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters")
  @Schema(example = "Dimcho")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Size(min = 2, max = 40, message = "Last name must be between 2 and 40 characters")
  @Schema(example = "Yasenov")
  private String lastName;

  @NotBlank(message = "Content cannot be blank")
  @Size(max = 1_000, message = "Content must be at max 1000 characters")
  @Schema(example = "Very good room, I would recommend to all my friends!")
  private String content;
}
