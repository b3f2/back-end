package com.backend.api.controller.auth;

import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이미 가입되어 있는 회원이 로그인을 한다.")
    public void test1() throws Exception {
        // given
        userRepository.save(userCreate());

        LoginRequest request = LoginRequest.builder()
                .email("userTest@gmail.com")
                .password("1111")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expect
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("로그인 시 AccessTokne을 헤더에 발급한다.")
    public void test2() throws Exception {
        // given
        userRepository.save(userCreate());
        LoginRequest request = LoginRequest.builder()
                .email("userTest@gmail.com")
                .password("1111")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expect
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 시 refresh Token을 쿠키에 발급한다.")
    public void test3() throws Exception {
        // given
        userRepository.save(userCreate());
        LoginRequest request = LoginRequest.builder()
                .email("userTest@gmail.com")
                .password("1111")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expect
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(cookie().exists("Authorization-refresh"))
                .andDo(print());
    }

    private User userCreate() {
        return User.builder()
                .email("userTest@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("user")
                .address(Address.builder()
                        .city("서울시")
                        .street("도봉로 106길 23")
                        .zipcode("102동 208호")
                        .build())
                .birth("20000418")
                .gender(Gender.MAN)
                .role(Role.ROLE_USER)
                .build();
    }

}