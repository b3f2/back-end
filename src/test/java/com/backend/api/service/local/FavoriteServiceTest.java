package com.backend.api.service.local;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.FavoriteLocal;
import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import com.backend.api.response.local.FavoriteLocalNameResponse;
import com.backend.api.response.local.FavoriteResponse;
import com.backend.api.response.user.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FavoriteServiceTest extends ServiceTestSupport {

    @AfterEach
    void setUp() {
        favoriteLocalRepository.deleteAllInBatch();
        favoritesRepository.deleteAllInBatch();
        localRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private LoginResponse loginCreate(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
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

    @Test
    @DisplayName("폴더 생성")
    @Transactional
    void createFavorite() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        CreateFavorite favorite = CreateFavorite.builder()
                .name("맛집 폴더")
                .build();

        //when
        favoriteService.createFavorite(loginCreate(user), favorite);

        //then
        assertEquals(1, favoritesRepository.findAll().size());
        assertEquals("맛집 폴더", favoritesRepository.findAll().get(0).getName());
        assertEquals("user@gmail.com", favoritesRepository.findAll().get(0).getUser().getEmail());
    }

    @Test
    @DisplayName("폴더 내에 장소 두개 넣기")
    @Transactional
    void addLocalToFavorites() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        Local local1 = localCreate(user);
        localRepository.save(local1);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local1)
                .build());

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

        AddLocalToFavorite addLocalToFavorite = AddLocalToFavorite.builder()
                .localId(local2.getId())
                .build();

        //when
        favoriteService.addLocalToFavorites(loginCreate(user), favorites.getId(), addLocalToFavorite);

        //then
        List<FavoriteLocal> favoriteLocals = favoritesRepository.findAll().get(0).getFavoriteLocals();

        assertEquals("상일동 맛집", favoriteLocals.get(0).getFavorites().getName());
        assertEquals("햄버거 맛집", favoriteLocals.get(1).getLocal().getName());
        assertEquals(2L, favoriteLocalRepository.count());
        assertEquals(2L, favoriteLocals.size());
        assertEquals(1L, favoritesRepository.count());
    }

    @Test
    @DisplayName("폴더 내에 장소 두개 넣고 favoriteId 값으로 조회하기")
    @Transactional
    void getFavorite() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        Local local1 = localCreate(user);
        localRepository.save(local1);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local1)
                .build());

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

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local2)
                .build());

        //when
        FavoriteResponse favorite = favoriteService.getFavorite(favorites.getId());
        ObjectMapper ob = new ObjectMapper();
        String s = ob.writeValueAsString(favorite);
        log.info("favorite = {}",s );
        //then
        assertEquals("상일동 맛집", favorite.getName());
        assertEquals("카페 맛집", favorite.getLocal().get(0).getName());
        assertEquals(2L, favoriteLocalRepository.count());
        assertEquals(2L, favoritesRepository.findAll().get(0).getFavoriteLocals().size());
        assertEquals(1L, favoritesRepository.count());
    }

    @Test
    @DisplayName("사용자마다 폴더에 장소 하나씩 넣고 사용자 id로 폴더 조회하기")
    @Transactional
    void getFavoriteByUserId() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        Local local = localCreate(user);
        localRepository.save(local);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local)
                .build());


        User anotherUser = User.builder()
                .email("another@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("another")
                .address(Address.builder()
                        .city("서울시")
                        .street("천호대로 1234길")
                        .zipcode("405동 607호")
                        .build())
                .birth("19991225")
                .gender(Gender.WOMAN)
                .build();
        userRepository.save(anotherUser);

        Favorites favorites2 = Favorites.builder()
                .name("길동 맛집")
                .user(anotherUser)
                .build();
        favoritesRepository.save(favorites2);

        Local local2 = localCreate(user);
        localRepository.save(local2);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites2)
                .local(local2)
                .build());

        //when
        List<FavoriteLocalNameResponse> favorite1 = favoriteService.getFavoritesByUser(user.getId());
        List<FavoriteLocalNameResponse> favorite2 = favoriteService.getFavoritesByUser(anotherUser.getId());

        //then
        assertEquals("상일동 맛집", favorite1.get(0).getName());
        assertEquals("카페 맛집", favorite1.get(0).getLocalNames().get(0));
        assertEquals("길동 맛집", favorite2.get(0).getName());
        assertEquals("카페 맛집", favorite2.get(0).getLocalNames().get(0));
        assertEquals(2L, favoriteLocalRepository.count());
        assertEquals(2L, favoritesRepository.count());
        assertEquals(2L, localRepository.count());
    }

    @Test
    @DisplayName("폴더 이름 바꾸기")
    void updateFavoriteName() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        UpdateFavoriteName updateFavoriteName = UpdateFavoriteName.builder()
                .name("상일동 카페")
                .build();

        //when
        favoriteService.updateFavoriteName(loginCreate(user), favorites.getId(), updateFavoriteName);

        //then
        assertEquals("상일동 카페", favoritesRepository.findAll().get(0).getName());
    }

    @Test
    @DisplayName("폴더 삭제")
    void deleteFavorite() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        //when
        favoriteService.deleteFavorite(loginCreate(user), favorites.getId());

        //then
        assertEquals(0L, favoritesRepository.count());
    }

    @Test
    @DisplayName("폴더 안에 장소 두개 넣고 하나 삭제하기")
    @Transactional
    void DeleteLocalToFavorites() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        Local local = localCreate(user);
        localRepository.save(local);

        Local local2 = Local.builder()
                .x(String.valueOf(37.8999))
                .y(String.valueOf(126.9000))
                .address(Address.builder()
                        .city("서울시")
                        .street("천호대로 1234길")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("햄버거 맛집")
                .user(user)
                .build();
        localRepository.save(local2);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local)
                .build());

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local2)
                .build());

        DeleteLocalToFavorite deleteLocalToFavorite = DeleteLocalToFavorite.builder()
                .localId(local.getId())
                .build();

        //when
        favoriteService.deleteLocalToFavorites(loginCreate(user), favorites.getId(), deleteLocalToFavorite);

        //then
        assertEquals(1L, favoriteLocalRepository.count());
        assertEquals(2L, localRepository.count());
        assertEquals(1L, favoritesRepository.count());
        assertEquals("햄버거 맛집", favoriteLocalRepository.findAll().get(0).getLocal().getName());
        assertEquals(2L, favoritesRepository.findAll().get(0).getFavoriteLocals().size());
    }

    @Test
    @DisplayName("폴더 안에 장소 두개 넣고 폴더를 삭제하기")
    @Transactional
    void DeleteFavoriteWithFavoriteLocal() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Favorites favorites = Favorites.builder()
                .name("상일동 맛집")
                .user(user)
                .build();
        favoritesRepository.save(favorites);

        Local local = localCreate(user);
        localRepository.save(local);

        Local local2 = Local.builder()
                .x(String.valueOf(37.8999))
                .y(String.valueOf(126.9000))
                .address(Address.builder()
                        .city("서울시")
                        .street("천호대로 1234길")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("햄버거 맛집")
                .user(user)
                .build();
        localRepository.save(local2);

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local)
                .build());

        favoriteLocalRepository.save(FavoriteLocal.builder()
                .favorites(favorites)
                .local(local2)
                .build());

        //when
        favoriteService.deleteFavorite(loginCreate(user), favorites.getId());

        //then
        assertEquals(0, favoriteLocalRepository.count());
        assertEquals(2L, localRepository.count());
        assertEquals(0, favoritesRepository.count());
    }
}