package com.backend.api.request.post;

import com.backend.api.entity.post.Image;
import com.backend.api.entity.post.Post;
import com.backend.api.exception.CategoryNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.post.CategoryRepository;
import com.backend.api.repository.user.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
