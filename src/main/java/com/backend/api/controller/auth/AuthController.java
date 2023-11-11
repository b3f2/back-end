package com.backend.api.controller.auth;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.user.LoginRequest;
import com.backend.api.request.util.RefreshToken;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.TokenResponse;
import com.backend.api.service.auth.AuthService;
import com.backend.api.util.Login;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.api.jwt.JwtFilter.AUTHORIZATION_HEADER;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        TokenResponse token = authService.login(request);

        Cookie cookie = createCookie(token.getRefreshToken().getHeader(), token.getRefreshToken().getData());
        response.setHeader(AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        response.addCookie(cookie);

        return ApiResponse.ok(token);

    }

    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissueToken(RefreshToken refreshToken, HttpServletResponse response) {
        TokenResponse token = authService.reissueToken(refreshToken);

        Cookie cookie = createCookie(AUTHORIZATION_HEADER, token.getRefreshToken().getData());

        response.setHeader(AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());
        response.addCookie(cookie);

        return ApiResponse.ok(token);
    }

    @PostMapping("/logout")
    public ApiResponse<TokenResponse> logout(@Login LoginResponse loginResponse, HttpServletResponse response) {
        authService.logout(loginResponse);

        removeCookie(response);

        return ApiResponse.<TokenResponse>builder()
                .message("로그아웃 성공")
                .status(HttpStatus.OK)
                .build();
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization-refresh", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(RefreshToken.EXPIRATION_PERIOD);
        cookie.setHttpOnly(true);

        return cookie;
    }



}
