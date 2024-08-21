package com.tinqinacademy.comments.api.models.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDetailsOutput {

  @Schema(example = "Comment id")
  private String id;
  @Schema(example = "Lando")
  private String firstName;
  @Schema(example = "Norris")
  private String lastName;
  @Schema(example = "Best hotel ever.")
  private String content;
  @Schema(example = "User id")
  private String publishedBy;
  @Schema(example = "2024-07-12T08:47:49.450Z")
  private LocalDateTime publishDate;
  @Schema(example = "2024-07-12T08:47:49.450Z")
  private LocalDateTime lastEditedDate;
  @Schema(example = "User id")
  private String lastEditedBy;
}
