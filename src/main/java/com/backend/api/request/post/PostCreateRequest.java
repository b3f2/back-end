package com.backend.api.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요.")
    private Long categoryId;

    private Long userId;

    private List<MultipartFile> images = new ArrayList<>();

    @Builder
    public PostCreateRequest(String title, String content, Long categoryId, Long userId, List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.userId = userId;
        this.images = images;
    }
}
