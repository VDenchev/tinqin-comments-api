package com.tinqinacademy.comments.core.processors.deletecomment;

import com.tinqinacademy.comments.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.exceptions.EntityNotFoundException;
import com.tinqinacademy.comments.api.operations.deletecomment.input.DeleteCommentInput;
import com.tinqinacademy.comments.api.operations.deletecomment.operation.DeleteCommentOperation;
import com.tinqinacademy.comments.api.operations.deletecomment.output.DeleteCommentOutput;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import com.tinqinacademy.comments.persistence.repositories.CommentRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class DeleteCommentOperationProcessor extends BaseOperationProcessor implements DeleteCommentOperation {

  private final CommentRepository commentRepository;

  public DeleteCommentOperationProcessor(
      ConversionService conversionService, Validator validator,
      CommentRepository commentRepository
  ) {
    super(conversionService, validator);
    this.commentRepository = commentRepository;
  }

  @Override
  @Transactional
  public Either<ErrorOutput, DeleteCommentOutput> process(DeleteCommentInput input) {
    return validateInput(input)
        .flatMap(validInput ->
            Try.of(() -> {
                  log.info("Start deleteComment input: {}", validInput);

                  UUID commentId = UUID.fromString(validInput.getCommentId());
                  Comment comment = commentRepository.findById(commentId)
                      .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

                  commentRepository.delete(comment);

                  DeleteCommentOutput output = createOutput();
                  log.info("End deleteComment output: {}", output);
                  return output;
                })
                .toEither()
                .mapLeft(t -> Match(t).of(
                    customStatusCase(t, EntityNotFoundException.class, HttpStatus.NOT_FOUND),
                    customStatusCase(t, IllegalArgumentException.class, HttpStatus.UNPROCESSABLE_ENTITY),
                    defaultCase(t)
                ))
        );
  }

  private DeleteCommentOutput createOutput() {
    return DeleteCommentOutput.builder()
        .build();
  }
}
