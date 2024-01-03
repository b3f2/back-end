package com.backend.api.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class PostRequest {
    @NotBlank
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private Long user;

    @NotBlank
    private Date writedate;

    @NotBlank
    private String category;

    @Builder
    private PostRequest(Long id, String title, Long user, Date writedate, String category) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.writedate = writedate;
        this.category = category;
    }
}
