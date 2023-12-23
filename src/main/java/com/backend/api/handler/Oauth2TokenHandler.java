package com.backend.api.handler;


import com.backend.api.entity.user.User;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.jwt.TokenProvider;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.util.RefreshToken;
import com.backend.api.response.user.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static com.backend.api.jwt.JwtFilter.AUTHORIZATION_HEADER;

@RequiredArgsConstructor
public class Oauth2TokenHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String oauthId = "";


        if ("kakao".equals(registrationId)) {
            oauthId = (String) oauth2User.getAttribute("id");
        } else if ("naver".equals(registrationId)) {
            oauthId = (String) ((Map<?, ?>) oauth2User.getAttribute("response")).get("id");
        } else {
            throw new IOException("서버 내부 오류");
        }

        User user = userRepository.findByOauthId(oauthId).orElseThrow(UserNotFoundException::new);

        TokenResponse token = tokenProvider.createToken(user.getEmail());

        user.updateRefreshToken(token.getRefreshToken().getData());

        Cookie cookie = createCookie(token.getRefreshToken().getHeader(), token.getRefreshToken().getData());

        response.setHeader(AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        response.addCookie(cookie);

        // 처음 소셜 로그인으로 접속 시 추가정보 기입을 위해 HTTP 상태메세지를 남김
        if (user.getAddress() == null) {
            response.sendRedirect("/api/oauth2");
        } else {
            response.sendRedirect("/apu/oauth2/ok");
        }
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(RefreshToken.EXPIRATION_PERIOD);
        cookie.setHttpOnly(true);

        return cookie;
    }

}
