package com.tinqinacademy.comments.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import com.tinqinacademy.comments.persistence.repositories.CommentRepository;
import com.tinqinacademy.comments.rest.CommentsApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static com.tinqinacademy.comments.api.RestApiRoutes.DELETE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT_BY_ADMIN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CommentsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class SystemControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private CommentRepository commentRepository;

  @BeforeEach
  public void setUp() {
    jdbcTemplate.execute("TRUNCATE TABLE comments");
    jdbcTemplate.execute("""
            INSERT INTO comments (id, content, first_name, last_name, room_id, last_edited_by, published_by, publish_date, last_edited_date)
            VALUES
                ('cedf52ab-b74b-4caa-beec-661c68859b7f', 'Great stay, wonderful service!', 'John', 'Doe', '6164a442-33c0-4b00-8cf2-04e495b8dc49', 'da2b658e-6a78-47f9-a4a9-103e24176df0', 'da2b658e-6a78-47f9-a4a9-103e24176df0', '2024-08-01 10:30:00', '2024-08-02 11:00:00'),
                ('38733e91-bf7d-4b65-9d19-623cde1e87c9', 'Room was cozy and neat.', 'Isabella', 'Wright', '6164a442-33c0-4b00-8cf2-04e495b8dc49', 'a9652552-90cc-4cf4-a34e-c52b628d9436', 'a9652552-90cc-4cf4-a34e-c52b628d9436', '2024-08-21 23:50:00', '2024-08-21 23:59:00');
        """);
  }

  @AfterEach
  public void tearDown() {
    jdbcTemplate.execute("TRUNCATE TABLE comments");
  }

  @Test
  void updateCommentByAdmin_whenValidInput_shouldRespondWithOkAndCommentId() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Ivan")
        .lastName("Asenov")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .content("Redacted By Admin")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is("38733e91-bf7d-4b65-9d19-623cde1e87c9")));

    Comment updatedComment = commentRepository.findById(UUID.fromString("38733e91-bf7d-4b65-9d19-623cde1e87c9"))
        .orElseThrow();
    assertEquals(input.getContent(), updatedComment.getContent());
    assertEquals(input.getFirstName(), updatedComment.getFirstName());
    assertEquals(input.getLastName(), updatedComment.getLastName());
    assertEquals(input.getRoomId(), updatedComment.getRoomId().toString());
    assertEquals(input.getAdminId(), updatedComment.getLastEditedBy().toString());
    assertTrue(() -> updatedComment.getPublishDate().isBefore(updatedComment.getLastEditedDate()));
  }

  @Test
  void updateCommentByAdmin_whenBlankRoomId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomId("")
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenInvalidRoomId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomId("INVALID_ROOM_ID")
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Room id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("roomId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenNullRoomId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Room id must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("roomId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenFirstNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("PenchoPenchoPenchoPenchoPenchoPenchoPencho")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("First name must be between 2 and 40 characters long")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenFirstNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("P")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("First name must be between 2 and 40 characters long")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenBlankFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenNullFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("First name must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenLastNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("DimovDimovDimovDimovDimovDimovDimovDimov1")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Last name must be between 2 and 40 characters long")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenLastNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .lastName("D")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Last name must be between 2 and 40 characters long")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenBlankLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenNullLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Last name must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Content must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Content must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenContentTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Content must be a maximum of 1000 character long")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenInvalidAdminId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("INVALID ADMIN ID")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Admin user id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("adminId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenBlankAdminId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_whenNullAdminId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "38733e91-bf7d-4b65-9d19-623cde1e87c9")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Admin user id must not be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("adminId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_invalidCommentId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "INVALID_COMMENT_ID")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Comment id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("commentId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateCommentByAdmin_nonExistingCommentId_shouldRespondWithNotFoundAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .adminId("94c73315-c572-49f4-b671-83908dfbd83f")
        .roomId("01689b2f-fe9f-4458-97bd-fc8cd43c7229")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "b1b9123d-c15e-4e6d-8f80-f62195c35ca7")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Comment with an id of b1b9123d-c15e-4e6d-8f80-f62195c35ca7 not found")))
        .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
    Optional<Comment> commentMaybe = commentRepository.findById(UUID.fromString("b1b9123d-c15e-4e6d-8f80-f62195c35ca7"));
    assertTrue(commentMaybe.isEmpty());
  }

  @Test
  void deleteComment_whenValidCommentId_shouldRespondWithOkAndEmptyBody() throws Exception {
    mockMvc.perform(delete(DELETE_COMMENT, "38733e91-bf7d-4b65-9d19-623cde1e87c9"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty())
    ;
    Optional<Comment> commentMaybe = commentRepository.findById(UUID.fromString("38733e91-bf7d-4b65-9d19-623cde1e87c9"));
    assertTrue(commentMaybe.isEmpty());
  }

  @Test
  void deleteComment_notExistingCommentId_shouldRespondWithNotFoundAndErrorResult() throws Exception {
    mockMvc.perform(delete(DELETE_COMMENT, "b1b9123d-c15e-4e6d-8f80-f62195c35ca7"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors[0].message", is("Comment with an id of b1b9123d-c15e-4e6d-8f80-f62195c35ca7 not found")))
        .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
  }

  @Test
  void deleteComment_invalidCommentId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    mockMvc.perform(delete(DELETE_COMMENT, "INVALID_COMMENT_ID"))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.errors[0].message", is("Comment id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("commentId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }
}