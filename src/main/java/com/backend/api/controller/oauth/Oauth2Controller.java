package com.backend.api.controller.oauth;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.oauth2.OauthEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.oauth2.OauthService;
import com.backend.api.util.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final OauthService oauthService;

    @GetMapping("/oauth2")
    public ApiResponse oauth2() {
        return ApiResponse.builder()
                .message("첫 소셜 로그인 사용자 -> 추가 정보 기입 필요")
                .status(ACCEPTED)
                .build();
    }

    @PatchMapping("/addInformation")
    public ApiResponse addInformation(@Login LoginResponse response,@RequestBody OauthEdit oauthEdit) {
        oauthService.addInformation(response, oauthEdit);

        return ApiResponse.builder()
                .message("추가정보 기입 완료")
                .status(OK)
                .build();
    }

}
