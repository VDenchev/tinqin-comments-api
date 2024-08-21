package com.tinqinacademy.comments.api.operations.getcomments.input;

import com.tinqinacademy.comments.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetCommentsInput implements OperationInput {

  @Schema(example = "Room UUID")
  @UUID(message = "Room id has to be a valid UUID string")
  @NotBlank(message = "Room id must not be blank")
  private String roomId;

  @PositiveOrZero(message = "The page cannot be a negative number")
  private Integer pageNumber;

  @Positive(message = "The page size cannot be a negative number")
  private Integer pageSize;
}
