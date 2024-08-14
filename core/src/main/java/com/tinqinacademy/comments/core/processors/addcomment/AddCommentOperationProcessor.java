package com.tinqinacademy.comments.core.processors.addcomment;

import com.tinqinacademy.comments.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.models.output.CommentOutput;
import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.addcomment.operation.AddCommentOperation;
import com.tinqinacademy.comments.api.operations.addcomment.output.AddCommentOutput;
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
public class AddCommentOperationProcessor extends BaseOperationProcessor implements AddCommentOperation {

  private final CommentRepository commentRepository;

  public AddCommentOperationProcessor(
      ConversionService conversionService, Validator validator,
      CommentRepository commentRepository
  ) {
    super(conversionService, validator);
    this.commentRepository = commentRepository;
  }

  @Override
  public Either<ErrorOutput, AddCommentOutput> process(AddCommentInput input) {
    return validateInput(input)
        .flatMap((validInput) ->
            Try.of(() -> {
                  log.info("Start addComment input: {}", validInput);

                  Comment comment = convertInputToComment(validInput);

                  Comment savedComment = commentRepository.save(comment);

                  AddCommentOutput output = createOutput(savedComment);
                  log.info("End addComment output: {}", output);
                  return  output;
                })
                .toEither()
                .mapLeft(t -> Match(t).of(
                    customStatusCase(t, ConversionException.class, HttpStatus.UNPROCESSABLE_ENTITY),
                    customStatusCase(t, IllegalArgumentException.class, HttpStatus.UNPROCESSABLE_ENTITY),
                    defaultCase(t)
                ))
        );
  }

  private AddCommentOutput createOutput(Comment savedComment) {
    return AddCommentOutput.builder()
        .output(CommentOutput.builder()
            .id(savedComment.getId().toString())
            .build())
        .build();
  }

  private Comment convertInputToComment(AddCommentInput validInput) {
    Comment comment = conversionService.convert(validInput, Comment.class);
    comment.setLastEditedBy(UUID.randomUUID());
    return comment;
  }
}
