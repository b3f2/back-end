package com.backend.api.docs.local;

import com.backend.api.controller.local.LocalController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.util.Address;
import com.backend.api.request.local.CreateLocal;
import com.backend.api.response.local.LocalResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.LocalService;
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

public class LocalControllerDocTest extends RestDocsSupport {

    private final LocalService localService = mock(LocalService.class);

    @Override
    protected Object initController() {
        return new LocalController(localService);
    }

    @Test
    @DisplayName("장소 생성")
    void createLocal() throws Exception {
        //given
        CreateLocal local = CreateLocal.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.CAFE)
                .name("카페 맛집")
                .build();

        given(localService.createLocal(any(CreateLocal.class), any(LoginResponse.class)))
                .willReturn(LocalResponse.builder()
                        .name("카페 맛집")
                        .x(String.valueOf(37.5665))
                        .y(String.valueOf(126.9780))
                        .address(Address.builder()
                                .city("서울시")
                                .street("상암로 12길 34")
                                .zipcode("405동 607호")
                                .build())
                        .areaCategory(AreaCategory.CAFE)
                        .build());

        //expected
        mockMvc.perform(post("/api/locals")
                        .content(objectMapper.writeValueAsString(local))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("장소 저장 이름"),
                                fieldWithPath("x").type(JsonFieldType.STRING)
                                        .description("x 좌표"),
                                fieldWithPath("y").type(JsonFieldType.STRING)
                                        .description("y 좌표"),
                                fieldWithPath("address").type(JsonFieldType.OBJECT)
                                        .description("주소"),
                                fieldWithPath("address.city").type(JsonFieldType.STRING)
                                        .description("시군구"),
                                fieldWithPath("address.street").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("address.zipcode").type(JsonFieldType.STRING)
                                        .description("동호수"),
                                fieldWithPath("areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리")),
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
                                        .description("장소 저장 이름"),
                                fieldWithPath("data.x").type(JsonFieldType.STRING)
                                        .description("x 좌표"),
                                fieldWithPath("data.y").type(JsonFieldType.STRING)
                                        .description("y 좌표"),
                                fieldWithPath("data.address").type(JsonFieldType.OBJECT)
                                        .description("주소"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("시군구"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("동호수"),
                                fieldWithPath("data.areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"))
                ));
    }

    @Test
    @DisplayName("장소 단건 조회")
    void getLocal() throws Exception {
        //given
        given(localService.getLocal(any(Long.class)))
                .willReturn(LocalResponse.builder()
                        .name("카페 맛집")
                        .x(String.valueOf(37.5665))
                        .y(String.valueOf(126.9780))
                        .address(Address.builder()
                                .city("서울시")
                                .street("상암로 12길 34")
                                .zipcode("405동 607호")
                                .build())
                        .areaCategory(AreaCategory.CAFE)
                        .build());

        //expected
        mockMvc.perform(get("/api/locals/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-get-single",
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
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("장소 저장 이름"),
                                fieldWithPath("data.x").type(JsonFieldType.STRING)
                                        .description("x 좌표"),
                                fieldWithPath("data.y").type(JsonFieldType.STRING)
                                        .description("y 좌표"),
                                fieldWithPath("data.address").type(JsonFieldType.OBJECT)
                                        .description("주소"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("시군구"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("동호수"),
                                fieldWithPath("data.areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"))
                ));
    }

    @Test
    @DisplayName("모든 장소 조회")
    void getLocalList() throws Exception {
        //given
        given(localService.getLocalList())
                .willReturn(List.of(
                        LocalResponse.builder()
                                .name("카페 맛집")
                                .x(String.valueOf(37.5665))
                                .y(String.valueOf(126.9780))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 12길 34")
                                        .zipcode("405동 607호")
                                        .build())
                                .areaCategory(AreaCategory.CAFE)
                                .build(),
                        LocalResponse.builder()
                                .x(String.valueOf(12.3456))
                                .y(String.valueOf(789.1230))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 55길 66")
                                        .zipcode("666동 777호")
                                        .build())
                                .areaCategory(AreaCategory.RESTAURANT)
                                .name("음식 맛집")
                                .build(),
                        LocalResponse.builder()
                                .x(String.valueOf(15.1515))
                                .y(String.valueOf(35.3535))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 1길 3")
                                        .zipcode("101동 102호")
                                        .build())
                                .areaCategory(AreaCategory.CULTURE)
                                .name("괜찮은 영화관")
                                .build()));

        //expected
        mockMvc.perform(get("/api/locals")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-get-list",
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
                                        .description("장소 저장 이름"),
                                fieldWithPath("data[].x").type(JsonFieldType.STRING)
                                        .description("x 좌표"),
                                fieldWithPath("data[].y").type(JsonFieldType.STRING)
                                        .description("y 좌표"),
                                fieldWithPath("data[].address").type(JsonFieldType.OBJECT)
                                        .description("주소"),
                                fieldWithPath("data[].address.city").type(JsonFieldType.STRING)
                                        .description("시군구"),
                                fieldWithPath("data[].address.street").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data[].address.zipcode").type(JsonFieldType.STRING)
                                        .description("동호수"),
                                fieldWithPath("data[].areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"))
                ));
    }

    @Test
    @DisplayName("사용자가 저장한 장소 목록")
    void getUserLocal() throws Exception {
        //given
        given(localService.getLocalsByUser(any(Long.class)))
                .willReturn(List.of(
                        LocalResponse.builder()
                                .name("카페 맛집")
                                .x(String.valueOf(37.5665))
                                .y(String.valueOf(126.9780))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 12길 34")
                                        .zipcode("405동 607호")
                                        .build())
                                .areaCategory(AreaCategory.CAFE)
                                .build(),
                        LocalResponse.builder()
                                .x(String.valueOf(12.3456))
                                .y(String.valueOf(789.1230))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 55길 66")
                                        .zipcode("666동 777호")
                                        .build())
                                .areaCategory(AreaCategory.RESTAURANT)
                                .name("음식 맛집")
                                .build(),
                        LocalResponse.builder()
                                .x(String.valueOf(15.1515))
                                .y(String.valueOf(35.3535))
                                .address(Address.builder()
                                        .city("서울시")
                                        .street("상암로 1길 3")
                                        .zipcode("101동 102호")
                                        .build())
                                .areaCategory(AreaCategory.CULTURE)
                                .name("괜찮은 영화관")
                                .build()));

        //expected
        mockMvc.perform(get("/api/user/{userId}/locals", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-get-by-userId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")),
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
                                        .description("장소 저장 이름"),
                                fieldWithPath("data[].x").type(JsonFieldType.STRING)
                                        .description("x 좌표"),
                                fieldWithPath("data[].y").type(JsonFieldType.STRING)
                                        .description("y 좌표"),
                                fieldWithPath("data[].address").type(JsonFieldType.OBJECT)
                                        .description("주소"),
                                fieldWithPath("data[].address.city").type(JsonFieldType.STRING)
                                        .description("시군구"),
                                fieldWithPath("data[].address.street").type(JsonFieldType.STRING)
                                        .description("도로명 주소"),
                                fieldWithPath("data[].address.zipcode").type(JsonFieldType.STRING)
                                        .description("동호수"),
                                fieldWithPath("data[].areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"))
                ));
    }

    @Test
    @DisplayName("장소 삭제")
    void deleteLocal() throws Exception {
        //expected
        mockMvc.perform(delete("/api/locals/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-delete",
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
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"))));
        verify(localService, times(1)).deleteLocal(any(LoginResponse.class), any(Long.class));

    }
}
