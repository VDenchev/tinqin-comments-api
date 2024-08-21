package com.tinqinacademy.comments.core.converters;

import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.core.converters.base.BaseConverter;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddCommentInputToComment extends BaseConverter<AddCommentInput, Comment> {
  @Override
  protected Comment doConvert(AddCommentInput source) {

    return Comment.builder()
        .roomId(UUID.fromString(source.getRoomId()))
        .firstName(source.getFirstName())
        .lastName(source.getLastName())
        .content(source.getContent())
        .lastEditedBy(UUID.fromString(source.getUserId()))
        .publishedBy(UUID.fromString(source.getUserId()))
        .build();
  }
}
