package com.tinqinacademy.comments.api.contracts;

import com.tinqinacademy.comments.api.operations.createcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.createcomment.output.AddCommentOutput;
import com.tinqinacademy.comments.api.operations.getcomments.input.GetCommentsInput;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import org.springframework.stereotype.Service;

@Service
public interface HotelService {

  GetCommentsOutput getAllCommentsByRoom(GetCommentsInput input);
  AddCommentOutput addComment(AddCommentInput input);
  UpdateCommentOutput updateComment(UpdateCommentInput input);
}
