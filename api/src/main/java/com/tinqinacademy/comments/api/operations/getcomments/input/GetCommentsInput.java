package com.tinqinacademy.comments.api.operations.getcomments.input;

import com.tinqinacademy.comments.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
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
  private String roomId;
}
