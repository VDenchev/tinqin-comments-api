package com.tinqinacademy.comments.core.converters;

import com.tinqinacademy.comments.api.models.output.CommentDetailsOutput;
import com.tinqinacademy.comments.core.converters.base.BaseConverter;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentToCommentDetailsOutput extends BaseConverter<Comment, CommentDetailsOutput> {

  @Override
  protected CommentDetailsOutput doConvert(Comment source) {
    return CommentDetailsOutput.builder()
        .id(source.getId().toString())
        .content(source.getContent())
        .firstName(source.getFirstName())
        .lastName(source.getLastName())
        .publishDate(source.getPublishDate())
        .lastEditedDate(source.getLastEditedDate())
        .lastEditedBy(source.getLastEditedBy().toString())
        .build();
  }
}
