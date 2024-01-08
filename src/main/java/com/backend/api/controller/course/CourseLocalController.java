package com.backend.api.controller.course;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.course.CreateCourseLocal;
import com.backend.api.request.course.UpdateCourseLocal;
import com.backend.api.request.local.AddLocalToCourse;
import com.backend.api.response.course.CourseLocalResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.course.CourseLocalService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CourseLocalController {

    private final CourseLocalService courseLocalService;

    @PostMapping("/course-local")
    public ApiResponse<CourseLocalResponse> createCourseLocal(@Login LoginResponse loginResponse, @RequestBody @Valid CreateCourseLocal request) {
        return ApiResponse.ok(courseLocalService.createCourseLocal(loginResponse, request));
    }

    @PostMapping("/course-local/{courseId}")
    public ApiResponse<CourseLocalResponse> addLocalToCourse(@Login LoginResponse loginResponse, @PathVariable Long courseId, @RequestBody @Valid AddLocalToCourse request) {
        return ApiResponse.ok(courseLocalService.addLocalToCourse(loginResponse, courseId, request));
    }

    @GetMapping("/course-local/{courseId}")
    public ApiResponse<List<CourseLocalResponse>> getCourseLocalList(@PathVariable Long courseId) {
        return ApiResponse.ok(courseLocalService.getCourseLocalList(courseId));
    }

    //arrayList 라서 0부터 시작
    @PatchMapping("/course-local")
    public ApiResponse<List<CourseLocalResponse>> updateCourseLocal(@Login LoginResponse loginResponse, @RequestBody @Valid UpdateCourseLocal request) {
        return ApiResponse.ok(courseLocalService.updateCourseLocal(loginResponse, request));
    }
}