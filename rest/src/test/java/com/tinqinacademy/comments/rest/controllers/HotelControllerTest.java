package com.tinqinacademy.comments.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.comments.api.operations.createcomment.input.AddCommentInput;
import com.tinqinacademy.comments.api.operations.updatecomment.input.UpdateCommentInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.tinqinacademy.comments.api.RestApiRoutes.CREATE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.GET_COMMENTS;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void getComments_validRoomId_whenShouldReturnOkResponseWithAllRoomComments() throws Exception {
    mockMvc.perform(get(GET_COMMENTS, "100"))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.comments[0].id", is("1234124")))
        .andExpect(jsonPath("$.comments[0].firstName", is("Lando")))
        .andExpect(jsonPath("$.comments[0].lastName", is("Norris")));
  }

  @Test
  void createComment_whenValidInput_shouldAddCommentAndRespondWithCommentId() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isCreated())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.id", is("1234141")));
  }

  @Test
  void addComment_whenNullFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .lastName("Yasenov")
        .content("Content")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void addComment_whenLongFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("DimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimchoDimcho")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenBlankFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenFirstNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("A")
        .lastName("Yasenov")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenLastNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("YasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenovYasenov")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenBlankLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenLastNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Y")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenNullLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .content("Veri gut hotel room 10/10")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenContentIsTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .content("")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void addComment_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    AddCommentInput input = AddCommentInput.builder()
        .firstName("Dimcho")
        .lastName("Yasenov")
        .build();

    mockMvc.perform(post(CREATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void updateComment_whenValidContent_shouldRespondWithOkAndReturnCommentId() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Content")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.id", is("1234141")));
  }

  @Test
  void updateComment_whenContentTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.Veri git room and hotel, I would recumend it to mai frends and family.")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateComment_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .content("")
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateComment_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentInput input = UpdateCommentInput.builder()
        .build();

    mockMvc.perform(patch(UPDATE_COMMENT, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }
}
