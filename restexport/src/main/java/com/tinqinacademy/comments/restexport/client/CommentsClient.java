package com.tinqinacademy.comments.restexport.client;

import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.addcomment.output.AddCommentOutput;
import com.tinqinacademy.comments.api.operations.deletecomment.output.DeleteCommentOutput;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.output.UpdateCommentByAdminOutput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.tinqinacademy.comments.api.FeignClientApiRoutes.CREATE_COMMENT;
import static com.tinqinacademy.comments.api.FeignClientApiRoutes.DELETE_COMMENT;
import static com.tinqinacademy.comments.api.FeignClientApiRoutes.GET_COMMENTS;
import static com.tinqinacademy.comments.api.FeignClientApiRoutes.UPDATE_COMMENT;
import static com.tinqinacademy.comments.api.FeignClientApiRoutes.UPDATE_COMMENT_BY_ADMIN;

@Headers({"Content-Type: application/json"})
public interface CommentsClient {

  @RequestLine(GET_COMMENTS)
  GetCommentsOutput getComments(@Param String roomId, @Param Integer pageNumber, @Param Integer pageSize);

  @RequestLine(CREATE_COMMENT)
  AddCommentOutput addComment(@Param String roomId, AddCommentInput addCommentInput);

  @RequestLine(UPDATE_COMMENT)
  UpdateCommentOutput updateComment(@Param String commentId, UpdateCommentInput updateCommentInput);

  @RequestLine(UPDATE_COMMENT_BY_ADMIN)
  UpdateCommentByAdminOutput updateCommentByAdmin(@Param String commentId, UpdateCommentByAdminInput updateCommentByAdminInput);

  @RequestLine(DELETE_COMMENT)
  DeleteCommentOutput deleteComment(@Param String commentId);
}
