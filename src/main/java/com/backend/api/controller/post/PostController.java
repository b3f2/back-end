package com.backend.api.controller.post;

import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.entity.user.User;
import com.backend.api.request.post.PostCreateRequest;
import com.backend.api.request.post.PostUpdateRequest;
import com.backend.api.response.ApiResponse;
import com.backend.api.response.post.PostCreateResponse;
import com.backend.api.response.post.PostDetailResponse;
import com.backend.api.response.post.PostResponse;
import com.backend.api.response.post.PostUpdateResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.post.PostService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Stream.builder;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    @DisplayName("게시글 생성")
    public ApiResponse<PostCreateResponse> create(@Valid @Login LoginResponse loginResponse, @Valid @ModelAttribute final PostCreateRequest req, @RequestPart(value = "image", required = false) List<MultipartFile> images) throws IOException {
//        postService.create(loginResponse, req, images);
//        return ApiResponse.<PostResponse>builder().status(HttpStatus.CREATED).message("게시글 작성 성공").build();
        return ApiResponse.ok(postService.create(loginResponse, req, images));
    }

    @GetMapping("/posts")
    @DisplayName("게시글 목록 조회")
    public ApiResponse<List<PostResponse>> readAll(@Valid @ModelAttribute PostReadCondition cond) {
        return ApiResponse.ok(postService.readAll(cond));
    }

    @GetMapping("/posts/{id}")
    @DisplayName("게시글 단건 조회")
    public ApiResponse<PostDetailResponse> read(@PathVariable final Long id) {
        return ApiResponse.of(HttpStatus.OK, postService.read(id));
    }

    @DeleteMapping("/posts/{id}")
    @DisplayName("게시글 삭제")
    public ApiResponse<PostResponse> delete(@PathVariable Long id, @Valid @Login LoginResponse loginResponse) {
        postService.delete(id, loginResponse);
        return ApiResponse.<PostResponse>builder()
                .status(HttpStatus.OK)
                .message("게시글 삭제 성공")
                .build();
    }

    @PutMapping("/posts/{id}")
    @DisplayName("게시글 수정")
    public ApiResponse<PostUpdateResponse> update (@PathVariable Long id, @Valid @ModelAttribute PostUpdateRequest req, @Valid @Login LoginResponse loginResponse, @RequestPart(value = "image", required = false) List<MultipartFile> images) throws IOException {
        return ApiResponse.of(HttpStatus.OK, postService.update(id, loginResponse, req, images));
    }

    @PostMapping("/posts/{id}")
    @DisplayName("게시글 좋아요")
    public ApiResponse<String> likePost(@PathVariable Long id, @Valid @Login LoginResponse loginResponse) {
        return ApiResponse.ok(postService.updatePostLike(id, loginResponse));
    }
}