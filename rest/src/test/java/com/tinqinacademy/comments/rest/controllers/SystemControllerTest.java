package com.tinqinacademy.comments.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.comments.api.operations.updatecommentbyadmin.input.UpdateCommentByAdminInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.tinqinacademy.comments.api.RestApiRoutes.DELETE_COMMENT;
import static com.tinqinacademy.comments.api.RestApiRoutes.UPDATE_COMMENT_BY_ADMIN;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SystemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void updateCommentByAdmin_whenValidInput_shouldRespondWithOkAndCommentId() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
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
  void updateCommentByAdmin_whenBlankRoomNumber_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("")
        .firstName("Pencho")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenNullRoomNumber_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .firstName("Pencho")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenFirstNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("PenchoPenchoPenchoPenchoPenchoPenchoPencho")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenFirstNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("P")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenBlankFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenNullFirstName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .lastName("Dimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenLastNameTooLong_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("DimovDimovDimovDimovDimovDimovDimovDimovDimov")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenLastNameTooShort_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("D")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenBlankLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenNullLastName_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .content("Content")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenBlankContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("Dimov")
        .content("")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void updateCommentByAdmin_whenNullContent_shouldRespondWithUnprocessableEntityAndErrorResult() throws Exception {
    UpdateCommentByAdminInput input = UpdateCommentByAdminInput.builder()
        .roomNumber("100")
        .firstName("Pencho")
        .lastName("Dimov")
        .build();

    mockMvc.perform(put(UPDATE_COMMENT_BY_ADMIN, "100")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        );
  }

  @Test
  void deleteComment_whenValidCommentId_shouldRespondWithNoContent() throws Exception {
    mockMvc.perform(delete(DELETE_COMMENT, "100"))
        .andExpect(status().isNoContent());
  }
}