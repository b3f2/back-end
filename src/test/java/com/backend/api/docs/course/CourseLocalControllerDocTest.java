package com.backend.api.docs.course;

import com.backend.api.controller.course.CourseLocalController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.request.course.CreateCourseLocal;
import com.backend.api.request.course.UpdateCourseLocal;
import com.backend.api.request.local.AddLocalToCourse;
import com.backend.api.response.course.CourseLocalResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseLocalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseLocalControllerDocTest extends RestDocsSupport {

    private final CourseLocalService courseLocalService = mock(CourseLocalService.class);

    @Override
    protected Object initController() {
        return new CourseLocalController(courseLocalService);
    }

    @Test
    @DisplayName("CourseLocal 생성")
    void createCourseLocal() throws Exception {
        //given
        CreateCourseLocal request = CreateCourseLocal.builder()
                .courseId(1L)
                .localId(1L)
                .build();

        CourseLocalResponse response = CourseLocalResponse.builder()
                .id(1L)
                .courseId(1L)
                .localId(1L)
                .turn(0)
                .build();

        given(courseLocalService.createCourseLocal(any(LoginResponse.class), any(CreateCourseLocal.class)))
                .willReturn(response);

        //expected
        mockMvc.perform(post("/api/course-local")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseLocal-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID")
                                        .optional(),
                                fieldWithPath("localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("CourseLocal ID"),
                                fieldWithPath("data.courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID"),
                                fieldWithPath("data.localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID"),
                                fieldWithPath("data.turn").type(JsonFieldType.NUMBER)
                                        .description("코스 순서"))
                ));
    }

    @Test
    @DisplayName("코스에 장소 넣기")
    void addLocalInCourse() throws Exception {
        //given
        AddLocalToCourse request = AddLocalToCourse.builder()
                .localId(1L)
                .build();

        CourseLocalResponse response = CourseLocalResponse.builder()
                .id(1L)
                .courseId(1L)
                .localId(1L)
                .turn(0)
                .build();

        given(courseLocalService.addLocalToCourse(any(LoginResponse.class), any(Long.class), any(AddLocalToCourse.class)))
                .willReturn(response);

        //expected
        mockMvc.perform(post("/api/course-local/{courseId}", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseLocal-add-local",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("courseId").description("코스 ID")),
                        requestFields(
                                fieldWithPath("localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("CourseLocal ID"),
                                fieldWithPath("data.courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID"),
                                fieldWithPath("data.localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID"),
                                fieldWithPath("data.turn").type(JsonFieldType.NUMBER)
                                        .description("코스 순서"))
                ));
    }

    @Test
    @DisplayName("코스 id값으로 조회")
    void getCourseLocalById() throws Exception {
        //given
        CourseLocalResponse response1 = CourseLocalResponse.builder()
                .id(1L)
                .courseId(1L)
                .localId(1L)
                .turn(0)
                .build();

        CourseLocalResponse response2 = CourseLocalResponse.builder()
                .id(2L)
                .courseId(1L)
                .localId(2L)
                .turn(1)
                .build();

        given(courseLocalService.getCourseLocalList(any(Long.class)))
                .willReturn(List.of(response1, response2));

        //expected
        mockMvc.perform(get("/api/course-local/{courseId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseLocal-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("courseId").description("코스 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("CourseLocal ID"),
                                fieldWithPath("data[].courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID"),
                                fieldWithPath("data[].localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID"),
                                fieldWithPath("data[].turn").type(JsonFieldType.NUMBER)
                                        .description("코스 순서"))
                ));
    }

    @Test
    @DisplayName("코스 안에 담긴 장소 순서 변경")
    void updateCourseLocal() throws Exception {
        //given
        UpdateCourseLocal request = UpdateCourseLocal.builder()
                .courseId(1L)
                .localId(1L)
                .turn(1)
                .build();

        CourseLocalResponse response1 = CourseLocalResponse.builder()
                .id(1L)
                .courseId(1L)
                .localId(1L)
                .turn(0)
                .build();

        CourseLocalResponse response2 = CourseLocalResponse.builder()
                .id(2L)
                .courseId(1L)
                .localId(2L)
                .turn(1)
                .build();

        given(courseLocalService.updateCourseLocal(any(LoginResponse.class), any(UpdateCourseLocal.class)))
                .willReturn(List.of(response1, response2));

        //expected
        mockMvc.perform(patch("/api/course-local", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("courseLocal-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID")
                                        .optional(),
                                fieldWithPath("localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID")
                                        .optional(),
                                fieldWithPath("turn").type(JsonFieldType.NUMBER)
                                        .description("장소 순서[0, 1, 2, ...]")
                                        .optional()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("CourseLocal ID"),
                                fieldWithPath("data[].courseId").type(JsonFieldType.NUMBER)
                                        .description("코스 ID"),
                                fieldWithPath("data[].localId").type(JsonFieldType.NUMBER)
                                        .description("장소 ID"),
                                fieldWithPath("data[].turn").type(JsonFieldType.NUMBER)
                                        .description("코스 순서"))
                ));
    }
}
