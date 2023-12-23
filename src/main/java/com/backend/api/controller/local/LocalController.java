package com.backend.api.controller.local;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.local.CreateLocal;
import com.backend.api.response.local.LocalResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.LocalService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocalController {

    private final LocalService localService;

    //장소 생성
    @PostMapping("/locals")
    public ApiResponse<LocalResponse> createLocal(@Login LoginResponse loginResponse, @RequestBody @Valid CreateLocal request) {
        return ApiResponse.ok(localService.createLocal(request, loginResponse));
    }

    //모든 장소 가져오기
    @GetMapping("/locals")
    public ApiResponse<List<LocalResponse>> getLocalList() {
        return ApiResponse.ok(localService.getLocalList());
    }

    //장소 단건 가져오기
    @GetMapping("/locals/{id}")
    public ApiResponse<LocalResponse> getLocal(@PathVariable Long id) {
        return ApiResponse.ok(localService.getLocal(id));
    }

    //장소 삭제
    @DeleteMapping("/locals/{id}")
    public ApiResponse<LocalResponse> deleteLocal(@Login LoginResponse loginResponse, @PathVariable Long id) {
        localService.deleteLocal(loginResponse, id);
        return ApiResponse.<LocalResponse>builder()
                .status(HttpStatus.OK)
                .message("장소 제거 성공")
                .build();
    }

    //사용자가 저장한 장소 목록 조회
    @GetMapping("/user/{userId}/locals")
    public ApiResponse<List<LocalResponse>> getLocals(@PathVariable Long userId) {
        return ApiResponse.ok(localService.getLocalsByUser(userId));
    }
}
