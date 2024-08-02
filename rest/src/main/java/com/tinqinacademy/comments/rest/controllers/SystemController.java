package com.tinqinacademy.comments.rest.controllers;

import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.contracts.SystemService;
import com.tinqinacademy.comments.api.operations.deletecomment.input.DeleteCommentInput;
import com.tinqinacademy.comments.api.operations.deletecomment.operation.DeleteCommentOperation;
import com.tinqinacademy.comments.api.operations.deletecomment.output.DeleteCommentOutput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.output.UpdateCommentByAdminOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tinqinacademy.comments.api.RestApiRoutes.DELETE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT_BY_ADMIN;

@RestController
@RequiredArgsConstructor
public class SystemController {

  private final SystemService systemService;
  private final DeleteCommentOperation deleteCommentOperation;

  @Operation(summary = "Admin can edit any comment for a certain room")
  @ApiResponses(value = {
      @ApiResponse(
          description = "Editing the comment was successful",
          responseCode = "200"
      ),
      @ApiResponse(
          description = "Comment with provided id does not exist",
          responseCode = "404"
      ),
      @ApiResponse(
          description = "Validation error",
          responseCode = "400"
      ),
      @ApiResponse(
          description = "You don't have permission",
          responseCode = "403"
      ),
      @ApiResponse(
          description = "You are not authorized",
          responseCode = "401"
      )
  })
  @PutMapping(UPDATE_COMMENT_BY_ADMIN)
  public ResponseEntity<UpdateCommentByAdminOutput> updateComment(
      @PathVariable String commentId,
      @RequestBody @Valid UpdateCommentByAdminInput input
  ) {
    input.setCommentId(commentId);
    UpdateCommentByAdminOutput output = systemService.updateCommentByAdmin(input);

    return new ResponseEntity<>(output, HttpStatus.OK);
  }


  @Operation(summary = "Deletes a comment with the provided id")
  @ApiResponses(value = {
      @ApiResponse(
          description = "Successfully deleted comment",
          responseCode = "204"
      ),
      @ApiResponse(
          description = "Comment with provided id does not exist",
          responseCode = "404"
      ),
      @ApiResponse(
          description = "You don't have permission",
          responseCode = "403"
      ),
      @ApiResponse(
          description = "You are not authorized",
          responseCode = "401"
      )
  })
  @DeleteMapping(DELETE_COMMENT)
  public ResponseEntity<OperationOutput> deleteComment(
      @PathVariable(name = "commentId") DeleteCommentInput input
  ) {
    return deleteCommentOperation.process(input)
        .fold(
            errorOutput -> new ResponseEntity<>(errorOutput, errorOutput.getStatusCode()),
            operationOutput -> new ResponseEntity<>(operationOutput, HttpStatus.OK)
        );
  }
}
