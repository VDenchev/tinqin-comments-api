package com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input;

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
public class UpdateCommentByAdminInput implements OperationInput {

  @JsonIgnore
  @NotBlank(message = "Comment id must not be blank")
  @UUID(message = "Comment id has to be a valid UUID string")
  private String commentId;

  @NotBlank(message = "Room id must not be blank")
  @UUID(message = "Room id has to be a valid UUID string")
  private String roomId;

  @NotBlank(message = "Admin user id must not be blank")
  @UUID(message = "Admin user id has to be a valid UUID string")
  private String adminId;

  @NotBlank(message = "First name must not be blank")
  @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters long")
  @Schema(example = "Dimcho")
  private String firstName;

  @NotBlank(message = "Last name must not be blank")
  @Size(min = 2, max = 40, message = "Last name must be between 2 and 40 characters long")
  @Schema(example = "Dimchov")
  private String lastName;

  @NotBlank(message = "Content must not be blank")
  @Size(max = 1_000, message = "Content must be a maximum of 1000 character long")
  @Schema(example = "*Redacted by admin*")
  private String content;
}
