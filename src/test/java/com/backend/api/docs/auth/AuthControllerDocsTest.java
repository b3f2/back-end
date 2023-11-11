package com.backend.api.docs.auth;

import com.backend.api.controller.auth.AuthController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.entity.user.Role;
import com.backend.api.request.user.LoginRequest;
import com.backend.api.request.util.AccessToken;
import com.backend.api.request.util.RefreshToken;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.TokenResponse;
import com.backend.api.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthService authService = mock(AuthService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService);
    }

    @Test
    @DisplayName("로그인을 하는 API")
    public void test1() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("user@gmail.com")
                .password("1111")
                .build();

        given(authService.login(any(LoginRequest.class)))
                .willReturn(TokenResponse.builder()
                        .accessToken(AccessToken.builder()
                                .header("Authorization")
                                .data("Bearer + AccessToken")
                                .build())
                        .refreshToken(RefreshToken.builder()
                                .header("Authorization-refresh")
                                .data("Bearer + RefreshTokenData")
                                .build())
                        .build()
                );

        // expect
        mockMvc.perform(
                        post("/api/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("패스워드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.OBJECT)
                                        .description("AccessToken"),
                                fieldWithPath("data.accessToken.header").type(JsonFieldType.STRING)
                                        .description("Authorization"),
                                fieldWithPath("data.accessToken.data").type(JsonFieldType.STRING)
                                        .description("Bearer + AccessToken"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.OBJECT)
                                        .description("RefreshToken"),
                                fieldWithPath("data.refreshToken.header").type(JsonFieldType.STRING)
                                        .description("Authorization-refresh"),
                                fieldWithPath("data.refreshToken.data").type(JsonFieldType.STRING)
                                        .description("Bearer + RefreshTokenData")
                        )
                ));
    }

    @Test
    @DisplayName("RefreshToken를 재발급 받는 API")
    public void test2() throws Exception {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .header("Authorization-refresh")
                .data("RefreshToken")
                .build();

        given(authService.reissueToken(any(RefreshToken.class)))
                .willReturn(TokenResponse.builder()
                        .accessToken(AccessToken.builder()
                                .header("Authorization")
                                .data("Bearer + AccessToken")
                                .build())
                        .refreshToken(RefreshToken.builder()
                                .header("Authorization-refresh")
                                .data("Bearer + RefreshTokenData")
                                .build())
                        .build()
                );

        // expect
        mockMvc.perform(post("/api/reissue")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth-reissue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("header").type(JsonFieldType.STRING)
                                        .description("Authorization-refresh"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .description("Bearer + RefreshTokenData")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.OBJECT)
                                        .description("AccessToken"),
                                fieldWithPath("data.accessToken.header").type(JsonFieldType.STRING)
                                        .description("Authorization"),
                                fieldWithPath("data.accessToken.data").type(JsonFieldType.STRING)
                                        .description("Bearer + AccessToken"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.OBJECT)
                                        .description("RefreshToken"),
                                fieldWithPath("data.refreshToken.header").type(JsonFieldType.STRING)
                                        .description("Authorization-refresh"),
                                fieldWithPath("data.refreshToken.data").type(JsonFieldType.STRING)
                                        .description("Bearer + RefreshTokenData")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃 하는 API")
    public void test3() throws Exception {
        // given
        LoginResponse response = LoginResponse.builder()
                .email("user@gmail.com")
                .role(Role.ROLE_USER)
                .build();

        doNothing().when(authService).logout(response);

        // expect
        mockMvc.perform(post("/api/logout")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth-reissue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("Token에 담긴 사용자 이메일"),
                                fieldWithPath("role").type(JsonFieldType.STRING)
                                        .description("Token에 담긴 사용자 권한")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("로그아웃 시 데이터는 담기지 않음")
                        )
                ));
    }

}
