package com.backend.api.controller.course;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.course.CreateCourseReview;
import com.backend.api.request.course.UpdateCourseReview;
import com.backend.api.response.course.CourseReviewResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseReviewService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    //코스 리뷰 생성
    @PostMapping("/course-review")
    public ApiResponse<CourseReviewResponse> createCourseReview(@Login LoginResponse loginResponse, @RequestBody @Valid CreateCourseReview request) {
        return ApiResponse.ok(courseReviewService.createCourseReview(request, loginResponse));
    }

    //모든 코스 리뷰 가져오기
    @GetMapping("/course-review")
    public ApiResponse<List<CourseReviewResponse>> getCourseReviewList() {
        return ApiResponse.ok(courseReviewService.getList());
    }

    //코스 리뷰 단건 가져오기
    @GetMapping("/course-review/{id}")
    public ApiResponse<CourseReviewResponse> getCourseReviews(@PathVariable Long id) {
        return ApiResponse.ok(courseReviewService.getCourseReview(id));
    }

    //코스 리뷰 수정
    @PatchMapping("/course-review/{id}")
    public ApiResponse<CourseReviewResponse> updateCourseReview(@Login LoginResponse loginResponse, @PathVariable Long id, @RequestBody @Valid UpdateCourseReview request) {
        return ApiResponse.ok(courseReviewService.updateCourseReview(loginResponse, id, request));
    }

    //코스 리뷰 단건 삭제
    @DeleteMapping("/course-review/{id}")
    public ApiResponse<CourseReviewResponse> deleteCourseReview(@Login LoginResponse loginResponse, @PathVariable Long id) {
        courseReviewService.deleteCourseReview(loginResponse, id);
        return ApiResponse.<CourseReviewResponse>builder()
                .status(HttpStatus.OK)
                .message("코스 리뷰 제거 성공")
                .build();
    }

    //코스 리뷰 가져오기(사용자 id로)
    @GetMapping("/users/{userId}/course-review")
    public ApiResponse<List<CourseReviewResponse>> getCourseReviewByUserId(@PathVariable Long userId) {
        return ApiResponse.ok(courseReviewService.getCourseReviewByUserId(userId));
    }
}
