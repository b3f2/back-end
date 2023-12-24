package com.backend.api.docs.local;

import com.backend.api.controller.local.LocalReviewController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.request.local.CreateLocalReview;
import com.backend.api.request.local.UpdateLocalReview;
import com.backend.api.response.local.LocalReviewResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.LocalReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocalReviewControllerDocTest extends RestDocsSupport {

    private final LocalReviewService localReviewService = mock(LocalReviewService.class);

    @Override
    protected Object initController() {
        return new LocalReviewController(localReviewService);
    }

    @Test
    @DisplayName("장소 리뷰 생성")
    void createLocalReview() throws Exception {
        CreateLocalReview local = CreateLocalReview.builder()
                .content("코스 리뷰")
                .rating(5)
                .localId(1L)
                .build();

        given(localReviewService.createLocalReview(any(LoginResponse.class), any(CreateLocalReview.class)))
                .willReturn(LocalReviewResponse.builder()
                        .content("장소 리뷰")
                        .rating(5)
                        .build());

        //expected
        mockMvc.perform(post("/api/local-review")
                        .content(objectMapper.writeValueAsString(local))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("localReview-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("장소 리뷰 내용"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER)
                                        .description("평점"),
                                fieldWithPath("localId").type(JsonFieldType.NUMBER)
                                        .description("장소 아이디")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("장소 리뷰"),
                                fieldWithPath("data.rating").type(JsonFieldType.NUMBER)
                                        .description("평점"))
                ));
    }

    @Test
    @DisplayName("장소 리뷰 단건 조회")
    void getLocalReview() throws Exception {
        given(localReviewService.getLocalReview(any(Long.class)))
                .willReturn(LocalReviewResponse.builder()
                        .content("장소 리뷰")
                        .rating(5)
                        .build());

        //expected
        mockMvc.perform(get("/api/local-review/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("localReview-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("장소 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("코스 리뷰"),
                                fieldWithPath("data.rating").type(JsonFieldType.NUMBER)
                                        .description("평점"))));
    }

    @Test
    @DisplayName("사용자의 장소 리뷰 조회")
    void getLocalReviewByUserId() throws Exception {
        given(localReviewService.getLocalReviewByUserId(any(Long.class)))
                .willReturn(List.of(
                        LocalReviewResponse.builder()
                                .content("장소 리뷰")
                                .rating(5)
                                .build(),
                        LocalReviewResponse.builder()
                                .content("장소 리뷰2")
                                .rating(4)
                                .build(),
                        LocalReviewResponse.builder()
                                .content("장소 리뷰3")
                                .rating(3)
                                .build()));

        //expected
        mockMvc.perform(get("/api/user/{userId}/local-review", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("localReview-get-by-userId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("사용자 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING)
                                        .description("코스 리뷰"),
                                fieldWithPath("data[].rating").type(JsonFieldType.NUMBER)
                                        .description("평점"))));
    }

    @Test
    @DisplayName("장소 리뷰 편집")
    void updateLocalReview() throws Exception {
        given(localReviewService.updateLocalReview(any(LoginResponse.class), any(Long.class), any(UpdateLocalReview.class)))
                .willReturn(LocalReviewResponse.builder()
                        .content("편집된 장소 리뷰")
                        .rating(5)
                        .build());

        UpdateLocalReview updateLocalReview = UpdateLocalReview.builder()
                .content("코스 리뷰")
                .rating(0)
                .localId(1L)
                .build();

        //expected
        mockMvc.perform(patch("/api/local-review/{id}", 1L)
                        .content(objectMapper.writeValueAsString(updateLocalReview))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("localReview-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("장소 리뷰 ID")),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("수정 하려는 코스 리뷰 내용"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER)
                                        .description("수정 하려는 평점"),
                                fieldWithPath("localId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 남길 장소 아이디")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("수정된 코스"),
                                fieldWithPath("data.rating").type(JsonFieldType.NUMBER)
                                        .description("수정된 평점"))));
    }

    @Test
    @DisplayName("장소 리뷰 삭제")
    void deleteLocalReview() throws Exception {
        //expected
        mockMvc.perform(delete("/api/local-review/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("localReview-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("장소 리뷰 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"))));
        verify(localReviewService, times(1)).deleteLocalReview(any(LoginResponse.class), any(Long.class));
    }
}
