package com.tinqinacademy.comments.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.comments.api.operations.addcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import com.tinqinacademy.comments.persistence.entities.comment.Comment;
import com.tinqinacademy.comments.persistence.repositories.CommentRepository;
import com.tinqinacademy.comments.rest.CommentsApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.tinqinacademy.comments.api.RestApiRoutes.CREATE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.GET_COMMENTS;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CommentsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class HotelControllerTest {

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
  void getComments_validRoomId_ShouldReturnOkResponseWithAllRoomComments() throws Exception {
    mockMvc.perform(
            get(GET_COMMENTS, "6164a442-33c0-4b00-8cf2-04e495b8dc49")
                .param("pageNumber", "0")
                .param("pageSize", "10")
        )
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.comments[0].id", is("cedf52ab-b74b-4caa-beec-661c68859b7f")))
        .andExpect(jsonPath("$.comments[0].firstName", is("John")))
        .andExpect(jsonPath("$.comments[0].lastName", is("Doe")));
  }

  @Test
  void getComments_invalidRoomId_ShouldRespondWithUnprocessableEntity() throws Exception {
    mockMvc.perform(
            get(GET_COMMENTS, "NOT_A_VALID_UUID")
                .param("pageNumber", "0")
                .param("pageSize", "10")
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Room id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("roomId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void getComments_invalidPageNumber_ShouldRespondWithUnprocessableEntity() throws Exception {
    mockMvc.perform(
            get(GET_COMMENTS, "6164a442-33c0-4b00-8cf2-04e495b8dc49")
                .param("pageNumber", "-1")
                .param("pageSize", "10")
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("The page cannot be a negative number")))
        .andExpect(jsonPath("$.errors[0].field", is("pageNumber")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void getComments_pageNumberExceedsTotalPages_ShouldRespondWithBadRequest() throws Exception {
    mockMvc.perform(
            get(GET_COMMENTS, "6164a442-33c0-4b00-8cf2-04e495b8dc49")
                .param("pageNumber", "10")
                .param("pageSize", "10")
        )
        .andExpect(status().isBadRequest())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Page number exceeds the total number of pages")))
        .andExpect(jsonPath("$.statusCode", is("BAD_REQUEST")));
  }

  @Test
  void getComments_invalidPageSize_ShouldRespondWithUnprocessableEntity() throws Exception {
    mockMvc.perform(
            get(GET_COMMENTS, "6164a442-33c0-4b00-8cf2-04e495b8dc49")
                .param("pageNumber", "1")
                .param("pageSize", "0")
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("The page size cannot be a negative number")))
        .andExpect(jsonPath("$.errors[0].field", is("pageSize")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenValidInput_shouldAddCommentAndRespondWithCreatedAndCommentId() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isCreated())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenNullFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .lastName("Yasenov")
        .content("Content")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("First name cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));

  }

  @Test
  void addComment_whenLongFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("AVeryLongNameWhyWouldYouNameYourChildLikeThisLikeWhoNamesTheirChildThis")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("First name must be between 2 and 40 characters")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenBlankFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenFirstNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("A")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("First name must be between 2 and 40 characters")))
        .andExpect(jsonPath("$.errors[0].field", is("firstName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenLastNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("AVeryLongLastNameLikeWhoTfNamedThisPerson")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(jsonPath("$.errors[0].message", is("Last name must be between 2 and 40 characters")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenBlankLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenLastNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Y")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Last name must be between 2 and 40 characters")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenNullLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .content("Veri gut hotel room 10/10")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Last name cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("lastName")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenContentIsTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Content must be at max 1000 characters")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Content cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Content cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenInvalidUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Content")
        .userId("INVALID_USER_ID")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("User id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("userId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_whenBlankUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Content")
        .userId("")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_nullBlankUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Content")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "58823063-dcdc-418b-b176-f5d5faa72074")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void addComment_invalidRoomId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .userId("860426a4-53ef-4a22-9bf5-077e0c409bca")
        .content("Content")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "INVALID_ROOM_ID")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.errors[0].message", is("Room id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("roomId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenValidContentAndExistingCommentIdAndUserId_shouldRespondWithOkAndReturnCommentId() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Content 10020")
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.id", is("cedf52ab-b74b-4caa-beec-661c68859b7f")));
    Comment updatedComment = commentRepository.findById(UUID.fromString("cedf52ab-b74b-4caa-beec-661c68859b7f"))
        .orElseThrow();
    assertEquals(input.getContent(), updatedComment.getContent());
    assertEquals(input.getUserId(), updatedComment.getLastEditedBy().toString());
    assertTrue(() -> updatedComment.getPublishDate().isBefore(updatedComment.getLastEditedDate()));
  }

  @Test
  void updateComment_whenContentTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to maisVeri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to maisrecumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel")
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
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
  void updateComment_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("")
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Content cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Content cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("content")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenUserIsNotCommentPublisher_shouldRespondWithNotFoundAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .userId("fa2965ae-1a78-27f9-a4a9-108e24176df0")
        .content("Content")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Comment with an id of cedf52ab-b74b-4caa-beec-661c68859b7f not found")))
        .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
  }

  @Test
  void updateComment_whenInvalidUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .userId("INVALID_USER_ID")
        .content("Content")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("User id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("userId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenBlankUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .userId("")
        .content("Content")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenNullUserId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Content")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "cedf52ab-b74b-4caa-beec-661c68859b7f")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("User id cannot be blank")))
        .andExpect(jsonPath("$.errors[0].field", is("userId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }

  @Test
  void updateComment_whenWrongCommentId_shouldRespondWithNotFoundAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Content")
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "4eac76a3-4707-4507-ab52-5c31b86b72ca")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Comment with an id of 4eac76a3-4707-4507-ab52-5c31b86b72ca not found")))
        .andExpect(jsonPath("$.statusCode", is("NOT_FOUND")));
  }

  @Test
  void updateComment_whenInvalidCommentId_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Content")
        .userId("da2b658e-6a78-47f9-a4a9-103e24176df0")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "INVALID_COMMENT_ID")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.errors[0].message", is("Comment id has to be a valid UUID string")))
        .andExpect(jsonPath("$.errors[0].field", is("commentId")))
        .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")));
  }
}
