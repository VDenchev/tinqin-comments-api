package com.tinqinacademy.comments.services;

import com.tinqinacademy.comments.api.contracts.SystemService;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import com.tinqinacademy.comments.api.operations.deletecomment.input.DeleteCommentInput;
import com.tinqinacademy.comments.api.operations.deletecomment.output.DeleteCommentOutput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.output.UpdateCommentByAdminOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

  @Override
  public UpdateCommentByAdminOutput updateCommentByAdmin(UpdateCommentByAdminInput input) {
    log.info("Start updateCommentByAdmin input: {}", input);

    UpdateCommentByAdminOutput output = UpdateCommentByAdminOutput.builder()
        .output(CommentOutput.builder()
            .id("1234141")
            .build())
        .build();

    log.info("End updateCommentByAdmin output: {}", output);
    return output;
  }

  @Override
  public DeleteCommentOutput deleteComment(DeleteCommentInput input) {
    log.info("Start deleteComment input: {}", input);

    DeleteCommentOutput output = DeleteCommentOutput.builder()
        .build();

    log.info("End deleteComment output: {}", output);
    return output;
  }
}
