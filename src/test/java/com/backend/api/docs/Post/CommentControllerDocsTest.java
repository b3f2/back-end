package com.backend.api.docs.Post;

import com.backend.api.controller.comment.CommentController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.request.comment.CommentCreateRequest;
import com.backend.api.request.comment.CommentReadRequest;
import com.backend.api.request.comment.CommentUpdateRequest;
import com.backend.api.response.comment.CommentCreateResponse;
import com.backend.api.response.comment.CommentResponse;
import com.backend.api.response.comment.CommentUpdateResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.Comment.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerDocsTest extends RestDocsSupport {
    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    @DisplayName("댓글 작성 API")
    @Test
    void createComment() throws Exception {
        CommentCreateRequest req = CommentCreateRequest.builder()
                .postId(1L)
                .userId(1L)
                .content("content")
                .parentId(null)
                .build();

        given(commentService.create(any(LoginResponse.class), any(CommentCreateRequest.class)))
                .willReturn(
                        CommentCreateResponse.builder()
                                .id(1L)
                                .build()
                );

        mockMvc.perform(
                        post("/api/comments")
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER)
                                        .description("게시글 번호"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("사용자 번호"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER)
                                        .description("부모 댓글 Id")
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("댓글 id")
                        )
                ));
    }

    @DisplayName("댓글 조회 API")
    @Test
    void readAllComment() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        CommentReadRequest req = CommentReadRequest.builder()
                .postId(1L)
                .build();

        given(commentService.readAll(req))
                .willReturn(List.of(
                        CommentResponse.builder()
                                .commentId(1L)
                                .content("content1")
                                .createTime(now)
                                .nickname("nick")
                                .userId(1L)
                                .build(),
                        CommentResponse.builder()
                                .commentId(2L)
                                .content("content2")
                                .createTime(now)
                                .nickname("bob")
                                .userId(1L)
                                .build()
                ));

        mockMvc.perform(
                        get("/api/comments")
                                .param("postId", String.valueOf(1L))
                                .contentType(APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data.[]commentId").type(JsonFieldType.NUMBER)
                                        .description("댓글 id"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("data[].createTime").type(JsonFieldType.STRING)
                                        .description("작성일"),
                                fieldWithPath("data[].userId").type(JsonFieldType.NUMBER)
                                        .description("작성자 Id"),
                                fieldWithPath("data[].nickname").type(JsonFieldType.STRING)
                                        .description("작성자 닉네임")
                        )
                ));
    }

    @DisplayName("댓글 삭제 API")
    @Test
    void deleteComment() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                        delete("/api/comments/{id}", id)
                )
                .andExpect(status().isOk());
    }
    @DisplayName("댓글 수정 API")
    @Test
    void updateComment() throws Exception {
        CommentUpdateRequest req = CommentUpdateRequest.builder()
                .content("update content")
                .build();

        given(commentService.updateComment(any(LoginResponse.class), anyLong(), any(CommentUpdateRequest.class)))
                .willReturn(
                        CommentUpdateResponse.builder()
                                .id(1L)
                                .build()
                );

        mockMvc.perform(
                        put("/api/comments/{id}", 1L)
                                .content(objectMapper.writeValueAsString(req))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("수정 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("댓글 id")
                        )
                ));
    }

    @DisplayName("댓글 좋아요 및 취소 API")
    @Test
    void likeOrUnlikeComment() throws Exception{
        Long id = 1L;

        mockMvc.perform(
                        post("/api/comments/{id}", id)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
