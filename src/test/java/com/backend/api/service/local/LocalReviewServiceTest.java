package com.backend.api.service.local;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.local.LocalReview;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.exception.LocalReviewNotFoundException;
import com.backend.api.request.local.CreateLocalReview;
import com.backend.api.request.local.UpdateLocalReview;
import com.backend.api.response.local.LocalReviewResponse;
import com.backend.api.response.user.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalReviewServiceTest extends ServiceTestSupport {

    @AfterEach
    void setUp() {
        localReviewRepository.deleteAllInBatch();
        localRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("장소 리뷰 생성")
    void createLocalReview() {
        //given
        User user = userCreate();
        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Local local = localCreate(user);
        localRepository.save(local);

        //when
        CreateLocalReview localReview = CreateLocalReview.builder()
                .content("분위기 좋음")
                .rating(5)
                .localId(local.getId())
                .build();

        localReviewService.createLocalReview(loginResponse, localReview);

        //then
        assertEquals(1, localReviewRepository.count());
        assertEquals("분위기 좋음", localReviewRepository.findAll().get(0).getContent());
        assertEquals(5, localReviewRepository.findAll().get(0).getRating());
    }

    @Test
    @DisplayName("장소 리뷰 조회(localId)")
    void getLocalReviewById() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Local local = localCreate(user);
        localRepository.save(local);

        CreateLocalReview localReview = CreateLocalReview.builder()
                .content("샌드위치 맛집")
                .rating(4)
                .localId(local.getId())
                .build();

        localReviewService.createLocalReview(loginResponse, localReview);

        Long id = localReviewRepository.findAll().get(0).getId();

        //when
        LocalReviewResponse localReviewResponse = localReviewService.getLocalReview(id);

        //then
        assertEquals(1, localReviewRepository.count());
        assertEquals("샌드위치 맛집", localReviewResponse.getContent());
        assertEquals(4, localReviewResponse.getRating());

    }

    @Test
    @DisplayName("장소 리뷰 조회(userId)")
    void getLocalReviewByUserId() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Local local = localCreate(user);
        localRepository.save(local);

        CreateLocalReview localReview1 = CreateLocalReview.builder()
                .content("샐러드 맛집")
                .rating(3)
                .localId(local.getId())
                .build();

        CreateLocalReview localReview2 = CreateLocalReview.builder()
                .content("토스트 맛집")
                .rating(5)
                .localId(local.getId())
                .build();

        localReviewService.createLocalReview(loginResponse, localReview1);
        localReviewService.createLocalReview(loginResponse, localReview2);

        //when
        List<LocalReviewResponse> localReviewByUserId = localReviewService.getLocalReviewByUserId(user.getId());

        //then
        assertEquals(2, localReviewRepository.count());
        assertEquals("샐러드 맛집", localReviewByUserId.get(0).getContent());
        assertEquals(3, localReviewByUserId.get(0).getRating());
        assertEquals("토스트 맛집", localReviewByUserId.get(1).getContent());
        assertEquals(5, localReviewByUserId.get(1).getRating());
    }

    @Test
    @DisplayName("장소 리뷰 수정")
    void updateLocalReviewByUserId() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Local local = localCreate(user);
        localRepository.save(local);

        LocalReview localReview = LocalReview.builder()
                .content("맛집")
                .rating(3)
                .user(user)
                .local(local)
                .build();

        localReviewRepository.save(localReview);

        UpdateLocalReview updateLocalReview = UpdateLocalReview.builder()
                .content("샐러드 맛집")
                .rating(5)
                .localId(local.getId())
                .build();

        //when
        LocalReviewResponse getLocalReview = localReviewService.updateLocalReview(loginResponse, localReview.getId(), updateLocalReview);

        //then
        assertEquals(1, localReviewRepository.count());
        assertEquals("샐러드 맛집", getLocalReview.getContent());
        assertEquals(5, getLocalReview.getRating());
    }

    @Test
    @DisplayName("장소 리뷰 삭제")
    void deleteLocalReviewByUserId() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Local local = localCreate(user);
        localRepository.save(local);

        LocalReview localReview = LocalReview.builder()
                .content("맛집")
                .rating(3)
                .user(user)
                .local(local)
                .build();

        localReviewRepository.save(localReview);

        //when
        localReviewService.deleteLocalReview(loginResponse, localReview.getId());

        //then
        assertEquals(0, localReviewRepository.count());
    }

    @Test
    @DisplayName("없는 값 조회")
    void getEmpty() throws Exception{
        //expect
        Assertions.assertThrows(LocalReviewNotFoundException.class,
                () -> localReviewService.getLocalReview(1L));
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
}