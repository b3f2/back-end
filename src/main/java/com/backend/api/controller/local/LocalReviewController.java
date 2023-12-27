package com.backend.api.controller.local;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.local.CreateLocalReview;
import com.backend.api.request.local.UpdateLocalReview;
import com.backend.api.response.local.LocalReviewResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.LocalReviewService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocalReviewController {

    private final LocalReviewService localReviewService;

    @PostMapping("/local-review")
    public ApiResponse<LocalReviewResponse> createLocalReview(@Login LoginResponse loginResponse, @RequestBody @Valid CreateLocalReview request) {
        return ApiResponse.ok(localReviewService.createLocalReview(loginResponse, request));
    }

    @GetMapping("/local-review/{id}")
    public ApiResponse<LocalReviewResponse> getLocalReview(@PathVariable Long id) {
        return ApiResponse.ok(localReviewService.getLocalReview(id));
    }

    @PatchMapping("/local-review/{id}")
    public ApiResponse<LocalReviewResponse> createLocalReview(@Login LoginResponse loginResponse, @PathVariable Long id, @RequestBody @Valid UpdateLocalReview request) {
        return ApiResponse.ok(localReviewService.updateLocalReview(loginResponse, id, request));
    }

    @DeleteMapping("/local-review/{id}")
    public ApiResponse<LocalReviewResponse> deleteCourseReview(@Login LoginResponse loginResponse, @PathVariable Long id) {
        localReviewService.deleteLocalReview(loginResponse, id);
        return ApiResponse.<LocalReviewResponse>builder()
                .status(HttpStatus.OK)
                .message("코스 리뷰 제거 성공")
                .build();
    }

    @GetMapping("/user/{userId}/local-review")
    public ApiResponse<List<LocalReviewResponse>> getLocalReviewByUserId(@PathVariable Long userId) {
        return ApiResponse.ok(localReviewService.getLocalReviewByUserId(userId));
    }
}
