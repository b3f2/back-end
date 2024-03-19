package com.backend.api.controller.local;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class FavoriteControllerTest extends ControllerTestSupport {
    @AfterEach
    void setUp() {
        localRepository.deleteAllInBatch();
        favoritesRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
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

    @Test
    @DisplayName("폴더 생성")
    @WithMockCustomUser
    @Transactional
    void createFavorite() throws Exception {
        //given
        CreateFavorite favorite = CreateFavorite.builder()
                .name("맛집 폴더")
                .build();

        String json = objectMapper.writeValueAsString(favorite);

        //expected
        mockMvc.perform(post("/api/favorites")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        Favorites favorites = favoritesRepository.findAll().get(0);

        assertEquals(1L, favoritesRepository.count());
        assertEquals("맛집 폴더", favorites.getName());
        assertEquals("user@gmail.com", favorites.getUser().getEmail());
    }

    @Test
    @DisplayName("폴더 빈값 생성")
    @WithMockCustomUser
    void createEmptyFavorite() throws Exception {
        //given
        CreateFavorite favorites = CreateFavorite.builder()
                .name("")
                .build();

        String json = objectMapper.writeValueAsString(favorites);

        //expected
        mockMvc.perform(post("/api/favorites")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("폴더명을 입력해 주세요"))
                .andExpect(jsonPath("$.data").isEmpty());

        assertEquals(0L, favoritesRepository.count());
    }
    @Test
    @DisplayName("폴더 안에 장소 넣기")
    @Transactional
    @WithMockCustomUser
    void addLocalToFavorites() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();

        favoritesRepository.save(favorites);

        Local local = localCreate(user);

        localRepository.save(local);

        AddLocalToFavorite updateFavorite = AddLocalToFavorite.builder()
                .localId(local.getId())
                .build();

        String json = objectMapper.writeValueAsString(updateFavorite);

        //expected
        mockMvc.perform(post("/api/favorites/{id}/local", favorites.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        Favorites favoriteLocals = favoritesRepository.findAll().get(0);

        assertEquals(1L, favoritesRepository.count());
        assertEquals("상일동 맛집", favoriteLocals.getName());
        assertEquals("카페 맛집", favoriteLocals.getLocal().get(0).getName());
    }

    @Test
    @DisplayName("폴더 안에 장소 두개 넣기")
    @Transactional
    @WithMockCustomUser
    void addLocalsToFavorites() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();

        Favorites saved = favoritesRepository.save(favorites);

        Local local1 = localCreate(user);

        localRepository.save(local1);

        saved.addLocals(local1);

        Local local2 = Local.builder()
                .x(String.valueOf(37.8999))
                .y(String.valueOf(126.9000))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("햄버거 맛집")
                .user(user)
                .build();

        localRepository.save(local2);

        AddLocalToFavorite updateFavorite = AddLocalToFavorite.builder()
                .localId(local2.getId())
                .build();

        String json = objectMapper.writeValueAsString(updateFavorite);

        //expected
        mockMvc.perform(post("/api/favorites/{id}/local", favorites.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        Favorites favoriteLocals = favoritesRepository.findAll().get(0);

        assertEquals("상일동 맛집", favoriteLocals.getName());
        assertEquals("햄버거 맛집", favoriteLocals.getLocal().get(1).getName());
        assertEquals(2L, favorites.getLocal().size());
        assertEquals(1L, favoritesRepository.count());
    }

    @Test
    @DisplayName("폴더 단건 조회")
    @WithMockCustomUser
    void getFavorite() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Favorites favorites = Favorites.builder()
                .name("길동 맛집들")
                .user(user)
                .build();

        favoritesRepository.save(favorites);

        //expected
        mockMvc.perform(get("/api/favorites/{id}", favorites.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("길동 맛집들"));


    }

    @Test
    @DisplayName("없는 폴더 조회")
    void getEmptyFavorite() throws Exception {
        //expected
        mockMvc.perform(get("/api/favorites/{id}", 1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("해당 즐겨찾기 폴더가 없습니다."));

    }

    @Test
    @DisplayName("사용자의 폴더 목록 조회")
    @WithMockCustomUser
    void getListByUserId() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        List<Favorites> favorites = IntStream.range(1, 10)
                .mapToObj(i -> Favorites.builder()
                        .name("코스 " + i)
                        .user(user)
                        .build())
                .toList();

        favoritesRepository.saveAll(favorites);

        //expected
        mockMvc.perform(get("/api/user/{userId}/favorites", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("코스 1"))
                .andExpect(jsonPath("$.data[8].name").value("코스 9"));
    }

    @Test
    @DisplayName("없는 사용자의 폴더 목록 조회")
    void getEmptyListByUserId() throws Exception {
        //expected
        mockMvc.perform(get("/api/user/{userId}/favorites", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("폴더 이름 수정")
    @WithMockCustomUser
    void updateFavoriteName() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();

        favoritesRepository.save(favorites);

        UpdateFavoriteName updateFavorite = UpdateFavoriteName.builder()
                .name("길동 맛집")
                .build();

        String json = objectMapper.writeValueAsString(updateFavorite);

        //expected
        mockMvc.perform(patch("/api/favorites/{id}", favorites.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals("길동 맛집", favoritesRepository.findAll().get(0).getName());
    }

    @Test
    @DisplayName("다른 사용자가 수정")
    @WithMockCustomUser
    void updateAnotherUserFavorite() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        User anotherUser = User.builder()
                .email("another@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("another")
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .birth("20001105")
                .gender(Gender.MAN)
                .build();

        userRepository.save(anotherUser);

        Favorites favorite = Favorites.builder()
                .name("상일동 맛집")
                .user(anotherUser)
                .build();

        Favorites saved = favoritesRepository.save(favorite);

        Local local = localCreate(anotherUser);

        localRepository.save(local);

        saved.addLocals(local);

        UpdateFavoriteName updateFavorite = UpdateFavoriteName.builder()
                .name("폴더명 바꾸기")
                .build();

        String json = objectMapper.writeValueAsString(updateFavorite);

        //expected
        //anotherUser의 코스를 user@gmail 사용자가 수정을 시도하는 것이다.
        mockMvc.perform(patch("/api/favorites/{id}", favorite.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 사용자입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

        assertEquals("상일동 맛집", favoritesRepository.findAll().get(0).getName());
    }

    @Test
    @DisplayName("폴더 삭제")
    @WithMockCustomUser
    void deleteFavorite() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Favorites favorites = Favorites.builder()
                .name("빈 폴더")
                .user(user)
                .build();

        favoritesRepository.save(favorites);

        //expected
        mockMvc.perform(delete("/api/favorites/{id}", favorites.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(0L, favoritesRepository.count());
    }

    @Test
    @DisplayName("없는 폴더 삭제")
    @WithMockCustomUser
    void deleteEmptyFavorite() throws Exception {
        //expected
        mockMvc.perform(delete("/api/favorites/{id}", 1L)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("해당 즐겨찾기 폴더가 없습니다."));
    }

    @Test
    @DisplayName("폴더 안에 장소 두개 넣고 장소 하나 삭제하기")
    @Transactional
    @WithMockCustomUser
    void DeleteLocalToFavorites() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();

        favoritesRepository.save(favorites);

        Local local1 = localCreate(user);

        localRepository.save(local1);

        favorites.addLocals(local1);

        Local local2 = Local.builder()
                .x(String.valueOf(37.8999))
                .y(String.valueOf(126.9000))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("햄버거 맛집")
                .user(user)
                .build();

        localRepository.save(local2);

        favorites.addLocals(local2);

        DeleteLocalToFavorite deleteLocalToFavorite = DeleteLocalToFavorite.builder()
                .localId(local1.getId())
                .build();

        String json = objectMapper.writeValueAsString(deleteLocalToFavorite);

        //expected
        mockMvc.perform(delete("/api/favorites/{id}/local", favorites.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, favoritesRepository.count());
        assertEquals(2L, localRepository.count());

        List<Local> local = favoritesRepository.findAll().get(0).getLocal();

        assertEquals("햄버거 맛집", local.get(0).getName());
    }
}