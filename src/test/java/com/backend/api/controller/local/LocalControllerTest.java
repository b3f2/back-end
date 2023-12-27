package com.backend.api.controller.local;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.local.CreateLocal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LocalControllerTest extends ControllerTestSupport {

    @AfterEach
    void setUp() {
        localRepository.deleteAllInBatch();
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
    @DisplayName("정상적인 장소 생성을 하면 응답 바디를 리턴한다.")
    @WithMockCustomUser
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

        String json = objectMapper.writeValueAsString(local);

        //expected
        mockMvc.perform(post("/api/locals")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, localRepository.count());
        Local response = localRepository.findAll().get(0);
        assertEquals("카페 맛집", response.getName());
    }

    @Test
    @DisplayName("비정상적인 장소 생성을 하면 응답 바디를 리턴한다.")
    @WithMockCustomUser
    void createEmptyLocal() throws Exception {
        //given
        CreateLocal local = CreateLocal.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(null)
                .name("카페 맛집")
                .build();

        String json = objectMapper.writeValueAsString(local);

        //expected
        mockMvc.perform(post("/api/locals")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, localRepository.count());
    }

    @Test
    @DisplayName("장소 저장한 뒤 장소 단건 조회하기")
    @WithMockCustomUser
    void getLocal() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Local local = localCreate(user);

        localRepository.save(local);

        //expected
        mockMvc.perform(get("/api/locals/{id}", local.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.x").value("37.5665"))
                .andExpect(jsonPath("$.data.address.city").value("서울시"))
                .andExpect(jsonPath("$.data.name").value("카페 맛집"));

        assertEquals(1, localRepository.count());
    }

    @Test
    @DisplayName("없는 장소 단건 조회하기")
    @WithMockCustomUser
    void getEmptyLocal() throws Exception {
        //expected
        mockMvc.perform(get("/api/locals/{id}",1L)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("해당 지역이 없습니다."));
    }
    @Test
    @DisplayName("여러 장소를 저장한 뒤 List 조회하기")
    @WithMockCustomUser
    void getLocalList() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Local local = localCreate(user);

        Local local2 = Local.builder()
                .x(String.valueOf(12.3456))
                .y(String.valueOf(789.1230))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 55길 66")
                        .zipcode("666동 777호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("음식 맛집")
                .user(user)
                .build();

        Local local3 = Local.builder()
                .x(String.valueOf(15.1515))
                .y(String.valueOf(35.3535))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 1길 3")
                        .zipcode("101동 102호")
                        .build())
                .areaCategory(AreaCategory.CULTURE)
                .name("괜찮은 영화관")
                .user(user)
                .build();

        localRepository.saveAll(List.of(local, local2, local3));

        //expected
        mockMvc.perform(get("/api/locals")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("카페 맛집"))
                .andExpect(jsonPath("$.data[0].address.city").value("서울시"))
                .andExpect(jsonPath("$.data[2].x").value("15.1515"));
        assertEquals(3, localRepository.count());
    }

    @Test
    @DisplayName("장소 저장한 뒤 삭제하기")
    @WithMockCustomUser
    void deleteLocal() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Local local = localCreate(user);

        localRepository.save(local);

        //expected
        mockMvc.perform(delete("/api/locals/{id}", local.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(0, localRepository.count());
    }
}