package com.tinqinacademy.comments.core.processors.updatecomment;

import com.tinqinacademy.comments.api.base.BaseOperationProcessor;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.exceptions.EntityNotFoundException;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.operation.UpdateCommentOperation;
import com.tinqinacademy.comments.api.operations.updatecomment.output.UpdateCommentOutput;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import com.tinqinacademy.comments.persistence.repositories.CommentRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class UpdateCommentOperationProcessor extends BaseOperationProcessor implements UpdateCommentOperation {

  private final CommentRepository commentRepository;

  public UpdateCommentOperationProcessor(
      ConversionService conversionService, Validator validator,
      CommentRepository commentRepository
  ) {
    super(conversionService, validator);
    this.commentRepository = commentRepository;
  }

  @Override
  public Either<ErrorOutput, UpdateCommentOutput> process(UpdateCommentInput input) {
    return validateInput(input)
        .flatMap(validInput ->
            Try.of(() -> {
                  log.info("Start updateComment input: {}", validInput);

                  UUID commentId = UUID.fromString(validInput.getCommentId());
                  Comment oldComment = commentRepository.findById(commentId)
                      .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

                  Comment commentToUpdate = buildUpdatedComment(validInput, oldComment);

                  Comment updatedComment = commentRepository.save(commentToUpdate);

                  UpdateCommentOutput output = createOutput(updatedComment);
                  log.info("End updateComment output: {}", output);
                  return output;
                })
                .toEither()
                .mapLeft(t -> Match(t).of(
                    customStatusCase(t, EntityNotFoundException.class, HttpStatus.NOT_FOUND),
                    customStatusCase(t, ConversionException.class, HttpStatus.UNPROCESSABLE_ENTITY),
                    defaultCase(t)
                ))
        );
  }

  private Comment buildUpdatedComment(UpdateCommentInput validInput, Comment comment) {
    //TODO: Update lastEditedBy field
    return comment.toBuilder()
        .content(validInput.getContent())
        .build();
  }

  private UpdateCommentOutput createOutput(Comment updatedComment) {
    CommentOutput commentOutput = CommentOutput.builder()
        .id(updatedComment.getId().toString())
        .build();

    return UpdateCommentOutput.builder()
        .output(commentOutput)
        .build();
  }
}
