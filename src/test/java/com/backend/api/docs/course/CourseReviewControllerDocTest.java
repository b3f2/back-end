package com.backend.api.docs.course;

import com.backend.api.controller.course.CourseReviewController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.request.course.CreateCourseReview;
import com.backend.api.request.course.UpdateCourseReview;
import com.backend.api.response.course.CourseReviewResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseReviewService;
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

public class CourseReviewControllerDocTest extends RestDocsSupport {

    private final CourseReviewService courseReviewService = mock(CourseReviewService.class);

    @Override
    protected Object initController() {
        return new CourseReviewController(courseReviewService);
    }

    @Test
    @DisplayName("코스 리뷰 생성")
    void createCourseReview() throws Exception {
        CreateCourseReview course = CreateCourseReview.builder()
                .content("코스 리뷰")
                .rating(5)
                .courseId(1L)
                .build();

        given(courseReviewService.createCourseReview(any(CreateCourseReview.class), any(LoginResponse.class)))
                .willReturn(CourseReviewResponse.builder()
                        .content("코스 리뷰")
                        .rating(5)
                        .build());

        //expected
        mockMvc.perform(post("/api/course-review")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("코스 리뷰 내용")
                                        .optional(),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER)
                                        .description("평점")
                                        .optional(),
                                fieldWithPath("courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 아이디")
                                        .optional()),
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
                                        .description("평점"))
                ));
    }

    @Test
    @DisplayName("코스 리뷰 단건 조회")
    void getCourse() throws Exception {
        given(courseReviewService.getCourseReview(any(Long.class)))
                .willReturn(CourseReviewResponse.builder()
                        .content("코스 리뷰")
                        .rating(5)
                        .build());

        //expected
        mockMvc.perform(get("/api/course-review/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("코스 ID")),
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
    @DisplayName("모든 코스 리뷰 조회")
    void getCourseReviewList() throws Exception {
        given(courseReviewService.getList())
                .willReturn(List.of(
                        CourseReviewResponse.builder()
                                .content("코스 리뷰")
                                .rating(5)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰2")
                                .rating(4)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰3")
                                .rating(3)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰4")
                                .rating(2)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰5")
                                .rating(1)
                                .build()));

        //expected
        mockMvc.perform(get("/api/course-review")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-get-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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
    @DisplayName("사용자의 코스 리뷰 조회")
    void getCourseReviewByUserId() throws Exception {
        given(courseReviewService.getCourseReviewByUserId(any(Long.class)))
                .willReturn(List.of(
                        CourseReviewResponse.builder()
                                .content("코스 리뷰")
                                .rating(5)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰2")
                                .rating(4)
                                .build(),
                        CourseReviewResponse.builder()
                                .content("코스 리뷰3")
                                .rating(3)
                                .build()));

        //expected
        mockMvc.perform(get("/api/user/{userId}/course-review", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-get-by-userId",
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
    @DisplayName("코스 리뷰 편집")
    void updateUserCourse() throws Exception {
        given(courseReviewService.updateCourseReview(any(LoginResponse.class), any(Long.class), any(UpdateCourseReview.class)))
                .willReturn(CourseReviewResponse.builder()
                        .content("편집된 코스 리뷰")
                        .rating(5)
                        .build());

        UpdateCourseReview updateCourseReview = UpdateCourseReview.builder()
                .content("코스 리뷰")
                .rating(0)
                .courseId(1L)
                .build();

        //expected
        mockMvc.perform(patch("/api/course-review/{id}", 1L)
                        .content(objectMapper.writeValueAsString(updateCourseReview))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("코스 리뷰 ID")),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("수정 하려는 코스 리뷰 내용")
                                        .optional(),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER)
                                        .description("수정 하려는 평점")
                                        .optional(),
                                fieldWithPath("courseId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 남길 코스 아이디")
                                        .optional()),
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
    @DisplayName("코스 리뷰 삭제")
    void deleteUserCourse() throws Exception {

        //expected
        mockMvc.perform(delete("/api/course-review/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseReview-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("코스 리뷰 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"))));
        verify(courseReviewService, times(1)).deleteCourseReview(any(LoginResponse.class), any(Long.class));
    }
}
