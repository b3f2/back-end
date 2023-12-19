package com.backend.api.controller.comment;

import com.backend.api.controller.ApiResponse;
import com.backend.api.entity.post.Comment;
import com.backend.api.request.comment.CommentCreateRequest;
import com.backend.api.request.comment.CommentReadRequest;
import com.backend.api.request.comment.CommentUpdateRequest;
import com.backend.api.response.comment.CommentCreateResponse;
import com.backend.api.response.comment.CommentResponse;
import com.backend.api.response.comment.CommentUpdateResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.Comment.CommentService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    @DisplayName("댓글 조회")
    public ApiResponse<List<CommentResponse>> readAll(@Valid CommentReadRequest req) {
        return ApiResponse.ok(commentService.readAll(req));
    }

    @PostMapping("/comments")
    @DisplayName("댓글 작성")
    public ApiResponse<CommentCreateResponse> create(@Valid @Login LoginResponse loginResponse, @Valid @RequestBody CommentCreateRequest req) {
        return ApiResponse.ok(commentService.create(loginResponse, req));
    }

    @DeleteMapping("/comments/{id}")
    @DisplayName("댓글 삭제")
    public ApiResponse<CommentResponse> delete(@Valid @Login LoginResponse loginResponse, @PathVariable Long id){
        commentService.delete(loginResponse, id);
        return ApiResponse.<CommentResponse>builder()
                .status(HttpStatus.OK)
                .message("댓글 삭제 성공")
                .build();
    }

    @PutMapping("/comments/{id}")
    @DisplayName("댓글 수정")
    public ApiResponse<CommentUpdateResponse> update(@Valid @Login LoginResponse loginResponse, @PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request) {
        return ApiResponse.of(HttpStatus.OK, commentService.updateComment(loginResponse, id, request));
    }

    @PostMapping("/comments/{id}")
    @DisplayName("댓글 좋아요")
    public ApiResponse<String> likeComments(@PathVariable Long id, @Valid @Login LoginResponse loginResponse){
        return ApiResponse.ok(commentService.updateCommentLike(id, loginResponse));
    }
}
