package com.tinqinacademy.comments.api.contracts;

import com.tinqinacademy.comments.api.operations.deletecomment.input.DeleteCommentInput;
import com.tinqinacademy.comments.api.operations.deletecomment.output.DeleteCommentOutput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.output.UpdateCommentByAdminOutput;
import org.springframework.stereotype.Service;

@Service
public interface SystemService {

  UpdateCommentByAdminOutput updateCommentByAdmin(UpdateCommentByAdminInput input);
  DeleteCommentOutput deleteComment(DeleteCommentInput input);
}
