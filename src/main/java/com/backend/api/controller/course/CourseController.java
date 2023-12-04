package com.backend.api.controller.course;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.course.CreateCourse;
import com.backend.api.request.course.UpdateCourse;
import com.backend.api.response.course.CourseResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    //코스 생성
    @PostMapping("/courses")
    public ApiResponse<CourseResponse> createCourse(@Login LoginResponse loginResponse, @RequestBody @Valid CreateCourse request) {
        return ApiResponse.ok(courseService.createCourse(request, loginResponse));
    }

    //코스 목록 조회(ex 홈페이지에 여러개)
    @GetMapping("/courses")
    public ApiResponse<List<CourseResponse>> getCourses() {
        return ApiResponse.ok(courseService.getList());
    }

    //코스 단건 조회(코스 id 값으로)
    @GetMapping("/courses/{id}")
    public ApiResponse<CourseResponse> getCourse(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getCourse(id));
    }

    //코스 단건 수정(코스 id 값으로)
    @PatchMapping("/courses/{id}")
    public ApiResponse<CourseResponse> updateCourse(@Login LoginResponse loginResponse, @PathVariable Long id, @RequestBody @Valid UpdateCourse request) {
        return ApiResponse.ok(courseService.updateCourse(loginResponse, id, request));
    }

    //코스 단건 삭제(코스 id 값으로)
    @DeleteMapping("/courses/{id}")
    public ApiResponse<CourseResponse> deleteCourse(@Login LoginResponse loginResponse, @PathVariable Long id) {
        courseService.deleteCourse(loginResponse, id);
        return ApiResponse.<CourseResponse>builder()
                .status(HttpStatus.OK)
                .message("코스 제거 성공")
                .build();
    }

    //코스 목록 조회(사용자 id 값으로)
    @GetMapping("/users/{userId}/courses")
    public ApiResponse<List<CourseResponse>> getUserCourses(@PathVariable Long userId) {
        return ApiResponse.ok(courseService.getCoursesByUserId(userId));
    }
}