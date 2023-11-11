package com.backend.api.docs.user;

import com.backend.api.controller.user.UserController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.util.Address;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import com.backend.api.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @Test
    @DisplayName("신규 회원을 등록하는 API")
    public void test1() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("user@gmail.com")
                .password("userpassword")
                .nickName("user")
                .city("서울특별시")
                .street("도봉구 도봉로 106길 23")
                .zipcode("102동 208호")
                .birth("20000418")
                .gender("MAN")
                .build();

        given(userService.signup(any(JoinRequest.class)))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("user@gmail.com")
                        .password(passwordEncoder.encode("1111"))
                        .nickName("user")
                        .address(Address.builder()
                                .city("서울특별시")
                                .street("도봉구 도봉로 106길 23")
                                .zipcode("102동 208호")
                                .build())
                        .birth("20000418")
                        .gender(Gender.MAN)
                        .role(Role.ROLE_USER)
                        .refreshToken(null)
                        .build()
                );
        // expect
        mockMvc.perform(
                        post("/api/signup")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("city").type(JsonFieldType.STRING)
                                        .description("도로명주소"),
                                fieldWithPath("street").type(JsonFieldType.STRING)
                                        .description("지번"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING)
                                        .description("상세보기[관련지번,관할주민센터,상세건물명]"),
                                fieldWithPath("birth").type(JsonFieldType.STRING)
                                        .description("생년월일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("성별")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("도로명주소"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("지번"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("상세보기[관련지번,관할주민센터,상세건물명]"),
                                fieldWithPath("data.birth").type(JsonFieldType.STRING)
                                        .description("생년월일"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING)
                                        .description("등급"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("RefreshToken")

                        )
                ));
    }

    @Test
    @DisplayName("회원 본인의 정보를 조회하는 API")
    public void test2() throws Exception {
        // given
        given(userService.get(any(LoginResponse.class)))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("user@gmail.com")
                        .password(passwordEncoder.encode("1111"))
                        .nickName("user")
                        .address(Address.builder()
                                .city("서울특별시")
                                .street("도봉구 도봉로 106길 23")
                                .zipcode("102동 208호")
                                .build())
                        .birth("20000418")
                        .gender(Gender.MAN)
                        .role(Role.ROLE_USER)
                        .refreshToken(null)
                        .build()
                );

        // expect
        mockMvc.perform(get("/api/user/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-profile",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("도로명주소"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("지번"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("상세보기[관련지번,관할주민센터,상세건물명]"),
                                fieldWithPath("data.birth").type(JsonFieldType.STRING)
                                        .description("생년월일"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING)
                                        .description("등급"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("RefreshToken")
                        )
                ));
    }

    @Test
    @DisplayName("유저 정보를 수정하는 API")
    public void test3() throws Exception {
        // given
        UserEdit request = UserEdit.builder()
                .password("1234")
                .nickName("userEdit")
                .city("서울")
                .street("노원구 상계로3길 50")
                .zipcode("00호")
                .build();

        given(userService.edit(any(LoginResponse.class), any(UserEdit.class)))
                .willReturn(UserResponse.builder()
                        .id(1L)
                        .email("user@gmail.com")
                        .password(passwordEncoder.encode("1234"))
                        .nickName("user")
                        .address(Address.builder()
                                .city("서울")
                                .street("노원구 상계로3길 50")
                                .zipcode("00호")
                                .build())
                        .birth("20000418")
                        .gender(Gender.MAN)
                        .role(Role.ROLE_USER)
                        .refreshToken(null)
                        .build()
                );

        // expect
        mockMvc.perform(
                        patch("/api/user/edit")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("비밀번호"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("닉네임"),
                                fieldWithPath("city").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("도로명주소"),
                                fieldWithPath("street").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("지번"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("상세보기[관련지번,관할주민센터,상세건물명]")
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("data.address.city").type(JsonFieldType.STRING)
                                        .description("도로명주소"),
                                fieldWithPath("data.address.street").type(JsonFieldType.STRING)
                                        .description("지번"),
                                fieldWithPath("data.address.zipcode").type(JsonFieldType.STRING)
                                        .description("상세보기[관련지번,관할주민센터,상세건물명]"),
                                fieldWithPath("data.birth").type(JsonFieldType.STRING)
                                        .description("생년월일"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                        .description("성별"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING)
                                        .description("등급"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("RefreshToken")

                        )
                ));
    }

    @Test
    @DisplayName("회원 탈퇴하는 API")
    public void test4() throws Exception {
        mockMvc.perform(delete("/api/user/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-delete",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("응답 데이터. 회원 탈퇴 시 추가 정보가 없음.")
                        )
                ));
    }
}
