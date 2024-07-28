package com.tinqinacademy.comments.api.operations.updatecomment.input;

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
@Builder(toBuilder = true)
@ToString
public class UpdateCommentInput {

  @JsonIgnore
  private String commentId;

  @NotBlank
  @Size(max = 10000)
  @Schema(example = "I changed my mind. This hotel is trash")
  private String content;
}
