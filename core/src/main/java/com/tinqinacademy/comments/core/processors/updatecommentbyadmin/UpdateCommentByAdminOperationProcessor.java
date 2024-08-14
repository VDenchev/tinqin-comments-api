package com.tinqinacademy.comments.core.processors.updatecommentbyadmin;

import com.tinqinacademy.comments.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.exceptions.EntityNotFoundException;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.operation.UpdateCommentByAdminOperation;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.output.UpdateCommentByAdminOutput;
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
public class UpdateCommentByAdminOperationProcessor extends BaseOperationProcessor implements UpdateCommentByAdminOperation {

  private final CommentRepository commentRepository;

  public UpdateCommentByAdminOperationProcessor(
      ConversionService conversionService, Validator validator,
      CommentRepository commentRepository
  ) {
    super(conversionService, validator);
    this.commentRepository = commentRepository;
  }

  @Override
  public Either<ErrorOutput, UpdateCommentByAdminOutput> process(UpdateCommentByAdminInput input) {
    return validateInput(input)
        .flatMap(validInput ->
            Try.of(() -> {
                  log.info("Start updateCommentByAdmin input: {}", validInput);

                  UUID commentId = UUID.fromString(validInput.getCommentId());
                  Comment oldComment = commentRepository.findById(commentId)
                      .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

                  Comment commentToUpdate = buildUpdatedComment(validInput, oldComment);

                  Comment updatedComment = commentRepository.save(commentToUpdate);

                  UpdateCommentByAdminOutput output = createOutput(updatedComment);
                  log.info("End updateCommentByAdmin output: {}", output);
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

  private Comment buildUpdatedComment(UpdateCommentByAdminInput validInput, Comment comment) {
    //TODO: Update lastEditedBy field
    return comment.toBuilder()
        .content(validInput.getContent())
        .firstName(validInput.getFirstName())
        .lastName(validInput.getLastName())
        .roomId(UUID.fromString(validInput.getRoomId()))
        .build();
  }

  private UpdateCommentByAdminOutput createOutput(Comment updatedComment) {
    CommentOutput commentOutput = CommentOutput.builder()
        .id(updatedComment.getId().toString())
        .build();

    return UpdateCommentByAdminOutput.builder()
        .output(commentOutput)
        .build();
  }
}
