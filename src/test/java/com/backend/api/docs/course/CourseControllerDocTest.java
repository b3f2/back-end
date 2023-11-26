package com.backend.api.docs.course;

import com.backend.api.controller.course.CourseController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.request.course.CreateCourse;
import com.backend.api.request.course.UpdateCourse;
import com.backend.api.response.course.CourseResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
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

@ExtendWith(RestDocumentationExtension.class) //junit5 사용시 문서 생성
class CourseControllerDocTest extends RestDocsSupport {

    private final CourseService courseService = mock(CourseService.class);

    @Override
    protected Object initController() {
        return new CourseController(courseService);
    }


    @Test
    @DisplayName("테스트")
    void test1() throws Exception {
        //expected
        mockMvc.perform(get("/api/restDocsTest")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index"));
    }

    @Test
    @DisplayName("코스 생성")
    void createCourse() throws Exception {
        CreateCourse course = CreateCourse.builder()
                .name("코스 1")
                .build();

        given(courseService.createCourse(any(CreateCourse.class), any(LoginResponse.class)))
                .willReturn(CourseResponse.builder()
                        .name("코스 1")
                        .build());

        //expected
        mockMvc.perform(post("/api/courses")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("course-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("코스 이름")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("코스 1"))
                ));
    }

    @Test
    @DisplayName("코스 단건 조회")
    void getCourse() throws Exception {
        given(courseService.getCourse(any(Long.class)))
                .willReturn(CourseResponse.builder()
                        .name("코스 1")
                        .build());

        //expected
        mockMvc.perform(get("/api/courses/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("course-get-single",
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
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("코스 1"))));

    }

    @Test
    @DisplayName("코스 목록 조회")
    void getCourseList() throws Exception {
        given(courseService.getList())
                .willReturn(List.of(
                        CourseResponse.builder()
                                .name("코스 1")
                                .build(),
                        CourseResponse.builder()
                                .name("코스 2")
                                .build(),
                        CourseResponse.builder()
                                .name("코스 3")
                                .build()
                ));

        //expected
        mockMvc.perform(get("/api/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("course-get-list",
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
                                fieldWithPath("data[].name").type(JsonFieldType.STRING)
                                        .description("코스 목록"))));
    }

    @Test
    @DisplayName("내 코스 조회")
    void getUserCourse() throws Exception {
        given(courseService.getCoursesByUserId(any(Long.class)))
                .willReturn(List.of(
                        CourseResponse.builder()
                                .name("내 코스 1")
                                .build(),
                        CourseResponse.builder()
                                .name("내 코스 2")
                                .build(),
                        CourseResponse.builder()
                                .name("내 코스 3")
                                .build()
                ));

        //expected
        mockMvc.perform(get("/api/users/courses/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-course-get",
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
                                fieldWithPath("data[].name").type(JsonFieldType.STRING)
                                        .description("코스 목록"))));

    }

    @Test
    @DisplayName("코스 편집")
    void updateUserCourse() throws Exception {
        given(courseService.updateCourse(any(LoginResponse.class), any(Long.class), any(UpdateCourse.class)))
                .willReturn(CourseResponse.builder()
                        .name("편집된 코스")
                        .build());

        UpdateCourse updateCourse = UpdateCourse.builder()
                .name("코스 1")
                .build();

        //expected
        mockMvc.perform(patch("/api/courses/{id}", 1L)
                        .content(objectMapper.writeValueAsString(updateCourse))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("course-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("코스 ID")),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("코스 이름")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("수정된 코스"))));
    }

    @Test
    @DisplayName("코스 삭제")
    void deleteUserCourse() throws Exception {

        //expected
        mockMvc.perform(delete("/api/courses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("course-delete",
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
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"))));
        verify(courseService, times(1)).deleteCourse(any(LoginResponse.class), any(Long.class));
    }
}