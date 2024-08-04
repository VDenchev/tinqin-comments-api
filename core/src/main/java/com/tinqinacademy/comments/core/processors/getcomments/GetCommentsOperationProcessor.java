package com.tinqinacademy.comments.core.processors.getcomments;

import com.tinqinacademy.comments.api.base.BaseOperationProcessor;
import com.tinqinacademy.comments.api.errors.ErrorOutput;
import com.tinqinacademy.comments.api.exceptions.InvalidPageNumberException;
import com.tinqinacademy.comments.api.models.output.CommentDetailsOutput;
import com.tinqinacademy.comments.api.operations.getcomments.input.GetCommentsInput;
import com.tinqinacademy.comments.api.operations.getcomments.operation.GetCommentsOperation;
import com.tinqinacademy.comments.api.operations.getcomments.output.GetCommentsOutput;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import com.tinqinacademy.comments.persistence.repositories.CommentRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class GetCommentsOperationProcessor extends BaseOperationProcessor implements GetCommentsOperation {

  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;

  private final CommentRepository commentRepository;

  public GetCommentsOperationProcessor(
      ConversionService conversionService, Validator validator,
      CommentRepository commentRepository
  ) {
    super(conversionService, validator);
    this.commentRepository = commentRepository;
  }

  @Override
  public Either<ErrorOutput, GetCommentsOutput> process(GetCommentsInput input) {
    return validateInput(input)
        .flatMap(validInput ->
            Try.of(() -> {
                  log.info("Start getComments input: {}", validInput);

                  Integer pageNumber = getPageNumberOrDefault(validInput);
                  Integer pageSize = getPageSizeOrDefault(validInput);
                  UUID roomId = UUID.fromString(validInput.getRoomId());

                  Pageable pageable = PageRequest.of(pageNumber, pageSize);
                  Page<Comment> result = commentRepository.findAllByRoomId(roomId, pageable);

                  validatePageNumber(pageNumber, result);

                  Page<CommentDetailsOutput> outputPage = convertToPageOutput(result);
                  GetCommentsOutput output = createOutput(outputPage);
                  log.info("End getComments input: {}", output);
                  return output;
                })
                .toEither()
                .mapLeft(t -> Match(t).of(
                    customStatusCase(t, IllegalArgumentException.class, HttpStatus.UNPROCESSABLE_ENTITY),
                    customStatusCase(t, InvalidPageNumberException.class, HttpStatus.BAD_REQUEST),
                    defaultCase(t)
                ))
        );
  }

  private Integer getPageNumberOrDefault(GetCommentsInput input) {
    if (input.getPageNumber() == null) {
      return DEFAULT_PAGE_NUMBER;
    }
    return input.getPageNumber();
  }

  private Integer getPageSizeOrDefault(GetCommentsInput input) {
    if (input.getPageSize() == null) {
      return DEFAULT_PAGE_SIZE;
    }
    return input.getPageSize();
  }

  private void validatePageNumber(Integer pageNumber, Page<Comment> result) {
    if (pageNumber != 0 && pageNumber >= result.getTotalPages()) {
      throw new InvalidPageNumberException("Page number exceeds the total number of pages");
    }
  }

  private Page<CommentDetailsOutput> convertToPageOutput(Page<Comment> commentPage) {
    List<CommentDetailsOutput> outputList = commentPage.stream()
        .map(c -> conversionService.convert(c, CommentDetailsOutput.class))
        .toList();
    return new PageImpl<>(outputList, commentPage.getPageable(), commentPage.getTotalElements());
  }

  private GetCommentsOutput createOutput(Page<CommentDetailsOutput> outputPage) {
    return GetCommentsOutput.builder()
        .page(outputPage)
        .build();
  }
}
