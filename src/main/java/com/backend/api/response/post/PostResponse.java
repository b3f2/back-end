package com.backend.api.response.post;

import com.backend.api.dto.post.ImageDto;
import com.backend.api.entity.post.Category;
import com.backend.api.entity.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private Long userid;
    private String content;
    private Category category;


    @Builder
    private PostResponse(Long id, String title, Long userid, String content, Category category) {
        this.id = id;
        this.title = title;
        this.userid = userid;
        this.content = content;
        this.category = category;
    }

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.userid= post.getId();
        this.content = post.getContent();
        this.category = post.getCategory();
    }

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .userid(post.getUser().getId())
                .content(post.getContent())
                .category(post.getCategory())
                .build();
    }
}
