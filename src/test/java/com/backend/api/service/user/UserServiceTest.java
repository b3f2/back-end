package com.backend.api.service.user;

import com.backend.api.ServiceTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.exception.InvalidSignUpException;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import com.backend.api.service.auth.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends ServiceTestSupport {

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(tokenProvider, passwordEncoder, userRepository);
    }


    @AfterEach
    void tearDown() {
        courseRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저가 회원가입을 한다. 비밀번호는 인코딩이 되어 저장이 된다.")
    public void test1() {
        // given

        JoinRequest joinRequest = JoinRequest.builder()
                .email("user@gmail.com")
                .password("1111")
                .nickName("user")
                .city("서울시")
                .street("도봉로 106길 23")
                .zipcode("102동 208호")
                .birth("20000418")
                .gender("MAN")
                .build();

        Gender man = Gender.valueOf("MAN");

        // when
        UserResponse userResponse = userService.signup(joinRequest);

        // then

        assertThat(userResponse)
                .extracting("email", "nickName", "address.city", "address.street", "address.zipcode", "birth", "gender")
                .contains("user@gmail.com", "user", "서울시", "도봉로 106길 23", "102동 208호", "20000418", man);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("사용자가 본인의 정보를 조회한다.")
    public void test2() {
        // given
        Gender man = Gender.valueOf("MAN");

        LoginResponse loginResponse = loginCreate();

        // when
        UserResponse response = userService.get(loginResponse);

        // then
        assertThat(response)
                .extracting("email", "nickName", "address.city", "address.street", "address.zipcode", "birth", "gender")
                .contains("user@gmail.com", "user", "서울시", "도봉로 106길 23", "102동 208호", "20000418", man);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("사용자가 본인의 비밀번호만 수정을 하고 나머지 값들은 null로 보내도 원래의 값이 유지가 된다.")
    public void test3() {
        // given
        LoginResponse loginResponse = loginCreate();

        Gender man = Gender.valueOf("MAN");

        UserEdit edit = UserEdit.builder()
                .password("4444")
                .build();

        // when
        UserResponse response = userService.edit(loginResponse, edit);

        // then
        assertThat(response)
                .extracting("email", "nickName", "address.city", "address.street", "address.zipcode", "birth", "gender")
                .contains("user@gmail.com", "user", "서울시", "도봉로 106길 23", "102동 208호", "20000418", man);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("사용자가 본인의 주소를 수정을 하고 나머지 값들은 null로 보내도 원래의 값이 유지가 된다.")
    public void test4() {
        // given
        LoginResponse loginResponse = loginCreate();

        Gender man = Gender.valueOf("MAN");

        UserEdit edit = UserEdit.builder()
                .city("서울특별시")
                .street("강동구")
                .zipcode("200길")
                .build();

        // when
        UserResponse response = userService.edit(loginResponse, edit);

        // then
        assertThat(response)
                .extracting("email", "nickName", "address.city", "address.street", "address.zipcode", "birth", "gender")
                .contains("user@gmail.com", "user", "서울특별시", "강동구", "200길", "20000418", man);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("존재하는 회원이 탈퇴를 한다.")
    public void test5() {
        // given
        LoginResponse loginResponse = loginCreate();
        // when
        userService.delete(loginResponse);

        // then
        assertThat(0).isEqualTo(userRepository.count());
    }

    @Test
    @DisplayName("이미 가입되어 있는 이메일로 회원가입을 시도하면 예외가 발생한다.")
    public void test6() {
        // given
        User user = userCreate();
        userRepository.save(user);

        JoinRequest joinRequest = JoinRequest.builder()
                .email("user@gmail.com")
                .password("2222")
                .nickName("userA")
                .city("서울시")
                .street("노원구 106길 23")
                .zipcode("102동 208호")
                .birth("20000418")
                .gender("MAN")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.signup(joinRequest))
                .isInstanceOf(InvalidSignUpException.class)
                .hasMessage("이미 가입되어 있는 이메일 입니다.");
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
                .build();
    }

    private LoginResponse loginCreate() {
        return LoginResponse.builder()
                .email("user@gmail.com")
                .role(Role.ROLE_USER)
                .build();
    }

}