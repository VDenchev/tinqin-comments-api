package com.tinqinacademy.comments.core.services;

import com.tinqinacademy.comments.api.contracts.HotelService;
import com.tinqinacademy.comments.api.models.output.CommentDetailsOutput;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import com.tinqinacademy.comments.api.operations.createcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.createcomment.output.AddCommentOutput;
import com.tinqinacademy.comments.api.operations.getcomments.input.GetCommentsInput;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {


  @Override
  public GetCommentsOutput getAllCommentsByRoom(GetCommentsInput input) {
    log.info("Start getAllCommentsByRoom input: {}", input.toString());

    List<CommentDetailsOutput> comments = new ArrayList<>();

    CommentDetailsOutput comment = CommentDetailsOutput.builder()
        .id("1234124")
        .firstName("Lando")
        .lastName("Norris")
        .content("Veri gid hotel 10/10, would recommend!")
        .publishDate(LocalDateTime.now())
        .lastUpdateDate(LocalDateTime.now())
        .build();

    comments.add(comment);
    comments.add(comment);
    comments.add(comment);
    comments.add(comment);
    comments.add(comment);

    GetCommentsOutput output = GetCommentsOutput.builder()
        .comments(comments)
        .build();

    log.info("End getAllCommentsByRoom output: {}", output.toString());
    return output;
  }

  @Override
  public AddCommentOutput addComment(AddCommentInput input) {
    log.info("End createComment input: {}", input.toString());

    AddCommentOutput output = AddCommentOutput.builder()
        .output(CommentOutput.builder()
            .id("1234141")
            .build())
        .build();

    log.info("End createComment output: {}", output.toString());
    return output;
  }

  @Override
  public UpdateCommentOutput updateComment(UpdateCommentInput input) {
    log.info("Start updateComment input: {}", input.toString());

    UpdateCommentOutput output = UpdateCommentOutput.builder()
        .output(CommentOutput.builder()
            .id("1234141")
            .build())
        .build();

    log.info("End updateComment output: {}", output.toString());
    return output;
  }
}
