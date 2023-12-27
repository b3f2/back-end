package com.backend.api.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostUpdateRequest {
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

    private List<String> ImgName;

    @Builder
    public PostUpdateRequest(String title, String content, List<String> ImgName) {
        this.title = title;
        this.content = content;
        this.ImgName = ImgName;
    }
}
