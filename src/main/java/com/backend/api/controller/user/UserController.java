package com.backend.api.controller.user;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import com.backend.api.service.user.UserService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@Valid @RequestBody JoinRequest request) {
        return ApiResponse.ok(userService.signup(request));
    }

    @GetMapping("/user/profile")
    public ApiResponse<UserResponse> get(@Login LoginResponse loginResponse) {
        return ApiResponse.ok(userService.get(loginResponse));
    }

    @PatchMapping("/user/edit")
    public ApiResponse<UserResponse> edit(@Login LoginResponse loginResponse, UserEdit userEdit) {
        return ApiResponse.ok(userService.edit(loginResponse, userEdit));
    }

    @DeleteMapping("/user/delete")
    public ApiResponse<UserResponse> delete(@Login LoginResponse loginResponse) {
        userService.delete(loginResponse);
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK)
                .message("회원 탈퇴 성공")
                .build();
    }

}
