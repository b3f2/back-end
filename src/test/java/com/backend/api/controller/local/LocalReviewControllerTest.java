package com.backend.api.controller.local;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.local.LocalReview;
import com.backend.api.entity.user.User;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.request.local.CreateLocalReview;
import com.backend.api.request.local.UpdateLocalReview;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LocalReviewControllerTest extends ControllerTestSupport {

    @AfterEach
    void setUp() {
        localReviewRepository.deleteAllInBatch();
        localRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("장소 리뷰 생성")
    @WithMockCustomUser
    void createLocalReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("코스 1")
                .user(user)
                .build();
        localRepository.save(local);

        CreateLocalReview localReview = CreateLocalReview.builder()
                .localId(local.getId())
                .content("장소 리뷰")
                .rating(5)
                .build();

        //expected
        mockMvc.perform(post("/api/local-review")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localReview))
                )
                .andDo(print())
                .andExpect(status().isOk());

        LocalReview localReview1 = localReviewRepository.findAll().get(0);

        assertEquals("장소 리뷰", localReview1.getContent());
        assertEquals(1L, localReviewRepository.count());
        assertEquals(1L, localRepository.count());
    }

    @Test
    @DisplayName("장소 리뷰 빈값 생성")
    @WithMockCustomUser
    void createEmptyLocalReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("장소 1")
                .user(user)
                .build();
        localRepository.save(local);

        CreateLocalReview localReview = CreateLocalReview.builder()
                .localId(local.getId())
                .content("")
                .rating(1)
                .build();

        //expected
        mockMvc.perform(post("/api/local-review")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localReview))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("리뷰 내용을 입력해 주세요"))
                .andExpect(jsonPath("$.data").isEmpty());

        assertEquals(0L, localReviewRepository.count());
    }

    @Test
    @DisplayName("장소 리뷰 단건 조회")
    @WithMockCustomUser
    void getLocalReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("장소 1")
                .user(user)
                .build();
        localRepository.save(local);

        LocalReview localReview = LocalReview.builder()
                .local(local)
                .content("장소 리뷰")
                .rating(5)
                .build();

        localReviewRepository.save(localReview);

        //expected
        mockMvc.perform(get("/api/local-review/{id}", localReview.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        LocalReview find = localReviewRepository.findAll().get(0);

        assertEquals("장소 리뷰", find.getContent());
        assertEquals(5, find.getRating());
        assertEquals(1L, userRepository.count());
        assertEquals(1L, localRepository.count());
        assertEquals(1L, localReviewRepository.count());
    }

    @Test
    @DisplayName("사용자의 장소 리뷰 조회")
    @WithMockCustomUser
    void getLocalReviewByUserId() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("장소 1")
                .user(user)
                .build();
        localRepository.save(local);

        List<LocalReview> locals = IntStream.range(1, 30)
                .mapToObj(i -> LocalReview.builder()
                        .content("리뷰 내용 " + i)
                        .rating(Math.min(i, 5))
                        .user(user)
                        .build())
                .toList();

        localReviewRepository.saveAll(locals);

        //expected
        mockMvc.perform(get("/api/user/{userId}/local-review", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        List<LocalReview> find = localReviewRepository.findAll();

        assertEquals("리뷰 내용 1", find.get(0).getContent());
        assertEquals(1, find.get(0).getRating());
        assertEquals("리뷰 내용 29", find.get(28).getContent());
        assertEquals(5, find.get(28).getRating());
        assertEquals(29L, localReviewRepository.count());
        assertEquals(1L, localRepository.count());
    }

    @Test
    @DisplayName("장소 리뷰 수정")
    @WithMockCustomUser
    void updateLocalReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("장소 1")
                .user(user)
                .build();
        localRepository.save(local);

        LocalReview localReview = LocalReview.builder()
                .local(local)
                .content("장소 리뷰")
                .rating(5)
                .user(user)
                .build();

        localReviewRepository.save(localReview);

        UpdateLocalReview updateLocalReview = UpdateLocalReview.builder()
                .content("수정 후 코스 리뷰")
                .rating(5)
                .localId(local.getId())
                .build();

        //expected
        mockMvc.perform(patch("/api/local-review/{id}", localReview.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLocalReview))
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, userRepository.count());
        assertEquals(1L, localRepository.count());
        assertEquals(1L, localReviewRepository.count());
        assertEquals("수정 후 코스 리뷰", localReviewRepository.findAll().get(0).getContent());
    }

    @Test
    @DisplayName("장소 리뷰 삭제")
    @WithMockCustomUser
    void deleteLocalReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name("장소 1")
                .user(user)
                .build();
        localRepository.save(local);

        LocalReview localReview = LocalReview.builder()
                .local(local)
                .content("장소 리뷰")
                .rating(5)
                .user(user)
                .build();

        localReviewRepository.save(localReview);

        //expected
        mockMvc.perform(delete("/api/local-review/{id}", localReview.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, userRepository.count());
        assertEquals(1L, localRepository.count());
        assertEquals(0L, localReviewRepository.count());
    }
}