package com.backend.api.controller.comment;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.request.comment.CommentCreateRequest;
import com.backend.api.request.comment.CommentReadRequest;
import com.backend.api.service.Comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerTestSupport {

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentService commentService;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @DisplayName("댓글 작성")
    void createTest() throws Exception {
        //given
        CommentCreateRequest req = new CommentCreateRequest("content", 1L, 1L, null);

        //when, then
        mockMvc.perform(
                post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateTest() throws Exception {
        //given
        String content = "update content";

        //when, then
        mockMvc.perform(
                put("/api/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("모든 댓글 조회")
    void findAllCommentsTest() throws Exception {
        //given
        CommentReadRequest req = new CommentReadRequest(1L);

        //when, then
        mockMvc.perform(
                get("/api/comments")
                        .param("postId", String.valueOf(req.getPostId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() throws Exception{
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                delete("/api/comments/{id}", id)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 좋아요 및 취소")
    void likeCommentTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                post("/api/comments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }
}
