package com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UpdateCommentByAdminInput {

  @JsonIgnore
  private String commentId;

  @NotBlank(message = "Room number must not be blank")
  private String roomNumber;

  @NotBlank(message = "First name must not be blank")
  @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters long")
  @Schema(example = "Dimcho")
  private String firstName;

  @NotBlank(message = "Last name must not be blank")
  @Size(min = 2, max = 40, message = "First name must be between 2 and 40 characters long")
  @Schema(example = "Dimcho")
  private String lastName;

  @NotBlank(message = "Content must not be blank")
  @Size(max = 10000, message = "Content must be a maximum of 10000 character long")
  @Schema(example = "Lmao")
  private String content;
}
