package com.tinqinacademy.comments.api.operations.createcomment.input;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddCommentInput implements OperationInput {

  @JsonIgnore
  private String roomId;

  @NotBlank
  @Size(min = 2, max = 40)
  @Schema(example = "Dimcho")
  private String firstName;

  @NotBlank
  @Size(min = 2, max = 40)
  @Schema(example = "Yasenov")
  private String lastName;

  @NotBlank
  @Size(max = 10_000)
  @Schema(example = "Veri git room and hotel, I would recumend it to mai frends and family.")
  private String content;
}
