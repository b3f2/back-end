package com.backend.api.service.auth;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.exception.InvalidLoginException;
import com.backend.api.request.user.LoginRequest;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.TokenResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest extends ServiceTestSupport {

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인을 하면 토큰을 생성한다.")
    public void test1() {
        // given
        User user = userCreate();

        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("1111")
                .build();

        // when
        TokenResponse token = authService.login(request);

        // then
        assertThat(token.getRefreshToken().getHeader()).isEqualTo("Authorization-refresh");
        assertThat(token.getRefreshToken().getData()).isNotNull();
        assertThat(token.getAccessToken().getHeader()).isEqualTo("Authorization");
        assertThat(token.getAccessToken().getData()).isNotNull();
    }

    @Test
    @DisplayName("잘못된 계정으로 로그인을 한다 -> 아이디")
    public void test3() {
        // given
        User user = userCreate();

        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("user2@gmail.com")
                .password("1111")
                .build();

        // when // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidLoginException.class)
                .hasMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");

    }

    @Test
    @DisplayName("잘못된 계정으로 로그인을 한다 -> 비밀번호")
    public void test4() {
        // given
        User user = userCreate();

        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email("user1@gmail.com")
                .password("1234")
                .build();

        // when // then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidLoginException.class)
                .hasMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");

    }

    private User userCreate() {
        return User.builder()
                .email("user1@gmail.com")
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

    private LoginResponse loginCreate(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}