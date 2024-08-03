package com.tinqinacademy.comments.api.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tinqinacademy.comments.api.base.OperationOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorOutput implements OperationOutput {

  private List<Error> errors;
  private HttpStatusCode statusCode;
}
