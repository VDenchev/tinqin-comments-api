package com.tinqinacademy.comments.rest.controllers;

import com.tinqinacademy.comments.api.base.OperationOutput;
import com.tinqinacademy.comments.api.contracts.HotelService;
import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.addcomment.operation.AddCommentOperation;
import com.tinqinacademy.comments.api.operations.addcomment.output.AddCommentOutput;
import com.tinqinacademy.comments.api.operations.getcomments.input.GetCommentsInput;
import com.tinqinacademy.comments.api.operations.getcomments.operation.GetCommentsOperation;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import static com.tinqinacademy.comments.api.RestApiRoutes.CREATE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.GET_COMMENTS;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT;

@RestController
@RequiredArgsConstructor
public class HotelController {

  private final HotelService hotelService;
  private final AddCommentOperation addCommentOperation;
  private final View error;

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
  public ResponseEntity<GetCommentsOutput> getComments(@PathVariable(name = "roomId") GetCommentsInput input) {
    GetCommentsOutput output = hotelService.getAllCommentsByRoom(input);

    return new ResponseEntity<>(output, HttpStatus.OK);
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
  public ResponseEntity<OperationOutput> addComment(
      @PathVariable String roomId,
      @RequestBody AddCommentInput input
  ) {
    input.setRoomId(roomId);
    return addCommentOperation.process(input)
        .fold(
            errorOutput -> new ResponseEntity<>(errorOutput, errorOutput.getStatusCode()),
            operationOutput -> new ResponseEntity<>(operationOutput, HttpStatus.CREATED)
        );
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
  @PatchMapping(UPDATE_COMMENT)
  public ResponseEntity<UpdateCommentOutput> updateComment(
      @PathVariable String commentId,
      @RequestBody @Valid UpdateCommentInput input
  ) {
    input = input.toBuilder()
        .commentId(commentId)
        .build();
    UpdateCommentOutput output = hotelService.updateComment(input);

    return new ResponseEntity<>(output, HttpStatus.OK);
  }
}
