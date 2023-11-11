package com.backend.api.controller.user;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("정상적인 회원가입을 하면 응답 바디를 리턴한다.")
    public void test1() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("user@gmail.com")
                .password("userpassword")
                .nickName("user")
                .city("서울시")
                .street("도봉로 106길 23")
                .zipcode("102동 208호")
                .birth("20000418")
                .gender("MAN")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expect
        mockMvc.perform(post("/api/signup")
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
    @WithMockCustomUser
    @DisplayName("로그인 후 회원이 본인의 계정을 조회한다.")
    public void test2() throws Exception {
        // expect
        mockMvc.perform(get("/api/user/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.email").value("userTest@gmail.com"))
                .andExpect(jsonPath("$.data.nickName").value("user"))
                .andExpect(jsonPath("$.data.address.city").value("집"))
                .andExpect(jsonPath("$.data.address.street").value("주소"))
                .andExpect(jsonPath("$.data.address.zipcode").value("호수"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("로그인 후 회원이 본인의 정보를 수정한다.")
    public void test3() throws Exception {
        // given
        UserEdit request = UserEdit.builder()
                .password("1234")
                .zipcode("집 수정")
                .street("주소 수정")
                .zipcode("호수 수정")
                .build();

        String json = objectMapper.writeValueAsString(request);
        // expect
        mockMvc.perform(patch("/api/user/edit")
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
    @WithMockCustomUser
    @DisplayName("로그인 후 회원이 탈퇴를 한다.")
    public void test4() throws Exception {
        // expect
        mockMvc.perform(delete("/api/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 탈퇴 성공"));
    }


    private User userCreate() {
        return User.builder()
                .email("user@gmail.com")
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