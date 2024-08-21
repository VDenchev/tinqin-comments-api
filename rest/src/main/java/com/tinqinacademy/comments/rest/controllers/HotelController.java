package com.tinqinacademy.comments.rest.controllers;

import com.tinqinacademy.comments.api.base.Output;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.addcomment.operation.AddCommentOperation;
import com.tinqinacademy.comments.api.operations.addcomment.output.AddCommentOutput;
import com.tinqinacademy.comments.api.operations.getcomments.input.GetCommentsInput;
import com.tinqinacademy.comments.api.operations.getcomments.operation.GetCommentsOperation;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.operation.UpdateCommentOperation;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import com.tinqinacademy.comments.rest.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tinqinacademy.comments.api.RestApiRoutes.CREATE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.GET_COMMENTS;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT;

@RestController
@RequiredArgsConstructor
public class HotelController extends BaseController {

  private final GetCommentsOperation getCommentsOperation;
  private final AddCommentOperation addCommentOperation;
  private final UpdateCommentOperation updateCommentOperation;

  @Operation(summary = "Retrieves all comments for the provided room")
  @ApiResponses(value = {
      @ApiResponse(
          description = "Successful retrieval of comments",
          responseCode = "200"
      ),
      @ApiResponse(
          description = "Room with the provided id does not exist",
          responseCode = "404"
      )
  })
  @GetMapping(GET_COMMENTS)
  public ResponseEntity<Output> getComments(
      @PathVariable(name = "roomId") String roomId,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize
  ) {
    GetCommentsInput input = GetCommentsInput.builder()
        .roomId(roomId)
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .build();
    Either<ErrorOutput, GetCommentsOutput> output = getCommentsOperation.process(input);

    return createResponse(output, HttpStatus.OK);
  }


  @Operation(summary = "Leaves a comment regarding a certain room")
  @ApiResponses(value = {
      @ApiResponse(
          description = "Successfully leaves a comment and returns its id",
          responseCode = "201"
      ),
      @ApiResponse(
          description = "Room with the provided id does not exist",
          responseCode = "404"
      ),
      @ApiResponse(
          description = "Validation error",
          responseCode = "400"
      )
  })
  @PostMapping(CREATE_COMMENT)
  public ResponseEntity<Output> addComment(
      @PathVariable String roomId,
      @RequestBody AddCommentInput input
  ) {
    input.setRoomId(roomId);
    Either<ErrorOutput, AddCommentOutput> output = addCommentOperation.process(input);
    return createResponse(output, HttpStatus.CREATED);
  }


  @Operation(summary = "User can edit his comment")
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
      )
  })
  @PatchMapping(UPDATE_COMMENT)
  public ResponseEntity<Output> updateComment(
      @PathVariable String commentId,
      @RequestBody UpdateCommentInput input
  ) {
    input = input.toBuilder()
        .commentId(commentId)
        .build();
    Either<ErrorOutput, UpdateCommentOutput> output = updateCommentOperation.process(input);

    return createResponse(output, HttpStatus.OK);
  }
}
