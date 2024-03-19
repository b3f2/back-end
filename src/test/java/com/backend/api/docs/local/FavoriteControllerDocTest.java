package com.backend.api.docs.local;

import com.backend.api.controller.local.FavoriteController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import com.backend.api.response.local.FavoriteNameResponse;
import com.backend.api.response.local.FavoriteResponse;
import com.backend.api.response.local.LocalResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.FavoriteService;
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

public class FavoriteControllerDocTest extends RestDocsSupport {

    private final FavoriteService favoriteService = mock(FavoriteService.class);

    @Override
    protected Object initController() {
        return new FavoriteController(favoriteService);
    }

    @Test
    @DisplayName("폴더 생성")
    void createFavorite() throws Exception {
        //given
        CreateFavorite favorite = CreateFavorite.builder()
                .name("강동구 맛집")
                .build();

        given(favoriteService.createFavorite(any(LoginResponse.class), any(CreateFavorite.class)))
                .willReturn(FavoriteNameResponse.builder()
                        .name("강동구 맛집")
                        .build());

        //expected
        mockMvc.perform(post("/api/favorites")
                        .content(objectMapper.writeValueAsString(favorite))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("즐겨찾기 폴더 이름")
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
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("즐겨찾기 폴더 이름"))
                ));
    }

    @Test
    @DisplayName("폴더에 장소 넣기")
    void addLocalInFavorite() throws Exception {
        //given
        AddLocalToFavorite request = AddLocalToFavorite.builder()
                .localId(1L)
                .build();

        FavoriteResponse response = FavoriteResponse.builder()
                .name("강동구 맛집")
                .local(List.of(LocalResponse.of(localCreate(userCreate()))))
                .build();

        given(favoriteService.addLocalToFavorites(any(LoginResponse.class), any(Long.class), any(AddLocalToFavorite.class)))
                .willReturn(response);

        //expected
        mockMvc.perform(post("/api/favorites/{favoriteId}/local", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-add-local",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("favoriteId").description("폴더 ID")),
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
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("폴더 이름"),
                                fieldWithPath("data.local").type(JsonFieldType.ARRAY)
                                        .description("장소"),
                                fieldWithPath("data.local[].name").type(JsonFieldType.STRING)
                                        .description("장소 이름"),
                                fieldWithPath("data.local[].address").type(JsonFieldType.OBJECT)
                                        .description("장소 주소"),
                                fieldWithPath("data.local[].address.city").type(JsonFieldType.STRING)
                                        .description("장소 주소 도시"),
                                fieldWithPath("data.local[].address.street").type(JsonFieldType.STRING)
                                        .description("장소 주소 도로명"),
                                fieldWithPath("data.local[].address.zipcode").type(JsonFieldType.STRING)
                                        .description("장소 주소 우편번호"),
                                fieldWithPath("data.local[].areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"),
                                fieldWithPath("data.local[].x").type(JsonFieldType.STRING)
                                        .description("장소 x좌표"),
                                fieldWithPath("data.local[].y").type(JsonFieldType.STRING)
                                        .description("장소 y좌표"))
                ));
    }

    @Test
    @DisplayName("폴더 id값으로 조회")
    void getFavoriteById() throws Exception {
        //given
        FavoriteResponse response = FavoriteResponse.builder()
                .name("상일동 맛집")
                .local(List.of(LocalResponse.of(localCreate(userCreate()))))
                .build();

        given(favoriteService.getFavorite(any(Long.class)))
                .willReturn(response);

        //expected
        mockMvc.perform(get("/api/favorites/{favoriteId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("favoriteId").description("폴더 ID")),
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
                                        .description("폴더 이름"),
                                fieldWithPath("data.local").type(JsonFieldType.ARRAY)
                                        .description("폴더에 저장된 장소"),
                                fieldWithPath("data.local[].name").type(JsonFieldType.STRING)
                                        .description("장소 이름"),
                                fieldWithPath("data.local[].address").type(JsonFieldType.OBJECT)
                                        .description("장소 주소"),
                                fieldWithPath("data.local[].address.city").type(JsonFieldType.STRING)
                                        .description("장소 주소 도시"),
                                fieldWithPath("data.local[].address.street").type(JsonFieldType.STRING)
                                        .description("장소 주소 도로명"),
                                fieldWithPath("data.local[].address.zipcode").type(JsonFieldType.STRING)
                                        .description("장소 주소 우편번호"),
                                fieldWithPath("data.local[].areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"),
                                fieldWithPath("data.local[].x").type(JsonFieldType.STRING)
                                        .description("장소 x좌표"),
                                fieldWithPath("data.local[].y").type(JsonFieldType.STRING)
                                        .description("장소 y좌표"))
                ));
    }

    @Test
    @DisplayName("폴더 이름 수정")
    void updateFavoriteName() throws Exception {
        //given
        UpdateFavoriteName updateFavoriteName = UpdateFavoriteName.builder()
                .name("편집된 폴더명")
                .build();

        given(favoriteService.updateFavoriteName(any(LoginResponse.class), any(Long.class), any(UpdateFavoriteName.class)))
                .willReturn(FavoriteNameResponse.builder()
                        .name("편집된 폴더명")
                        .build());

        //expected
        mockMvc.perform(patch("/api/favorites/{favoriteId}", 1L)
                        .content(objectMapper.writeValueAsString(updateFavoriteName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-update-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("favoriteId").description("폴더 ID")),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("폴더 이름")
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
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("폴더 이름"))
                ));
    }

    @Test
    @DisplayName("폴더 삭제")
    void deleteFavorite() throws Exception {
        //expected
        mockMvc.perform(delete("/api/favorites/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-delete",
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
        verify(favoriteService, times(1)).deleteFavorite(any(LoginResponse.class), any(Long.class));

    }

    @Test
    @DisplayName("폴더 안 장소 삭제")
    void deleteLocalToFavorite() throws Exception {
        DeleteLocalToFavorite deleteLocalToFavorite = DeleteLocalToFavorite.builder()
                .localId(1L)
                .build();

        //expected
        mockMvc.perform(delete("/api/favorites/{favoriteId}/local", 1L)
                        .content(objectMapper.writeValueAsString(deleteLocalToFavorite))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("local-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("favoriteId").description("폴더 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"))));

        verify(favoriteService, times(1)).deleteLocalToFavorites(any(LoginResponse.class), any(Long.class), any(DeleteLocalToFavorite.class));

    }

    @Test
    @DisplayName("폴더 사용자 id값으로 조회")
    void getFavoriteByUserId() throws Exception {
        //given
        List<FavoriteResponse> response = List.of(FavoriteResponse.builder()
                .name("상일동 맛집")
                .local(List.of(
                        LocalResponse.of(localCreate(userCreate())),
                        LocalResponse.of(localCreate2(userCreate()))
                        ))
                .build());

        given(favoriteService.getFavoritesByUser(any(Long.class)))
                .willReturn(response);


        //expected
        mockMvc.perform(get("/api/user/{userId}/favorites", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("favorite-get-by-userId",
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
                                        .description("장소 저장 이름"),
                                fieldWithPath("data[].local[].name").type(JsonFieldType.STRING)
                                        .description("장소 이름"),
                                fieldWithPath("data[].local[].address").type(JsonFieldType.OBJECT)
                                        .description("장소 주소"),
                                fieldWithPath("data[].local[].address.city").type(JsonFieldType.STRING)
                                        .description("장소 주소 도시"),
                                fieldWithPath("data[].local[].address.street").type(JsonFieldType.STRING)
                                        .description("장소 주소 도로명"),
                                fieldWithPath("data[].local[].address.zipcode").type(JsonFieldType.STRING)
                                        .description("장소 주소 우편번호"),
                                fieldWithPath("data[].local[].areaCategory").type(JsonFieldType.STRING)
                                        .description("장소 카테고리"),
                                fieldWithPath("data[].local[].x").type(JsonFieldType.STRING)
                                        .description("장소 x좌표"),
                                fieldWithPath("data[].local[].y").type(JsonFieldType.STRING)
                                        .description("장소 y좌표"))
                ));
    }

    private User userCreate() {
        return User.builder()
                .email("user@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("user")
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .birth("20001105")
                .gender(Gender.MAN)
                .build();
    }

    private Local localCreate(User user) {
        return Local.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.CAFE)
                .name("카페 맛집")
                .user(user)
                .build();
    }
    private Local localCreate2(User user){
        return Local.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("천호대로 99길 12")
                        .zipcode("101동 102호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("돈까스 맛집")
                .user(user)
                .build();
    }
}
