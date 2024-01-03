package com.backend.api.response.post;

import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.Image;
import com.backend.api.entity.post.Post;
import com.backend.api.response.comment.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostDetailResponse {
    private Long postId;
    private Long userId;
    private String nickname;
    private String content;
    private String title;
    private List<String> imgList;
    private List<Comment> commentList;
    private Long categoryId;
    private LocalDateTime createTime;
    private String categoryName;

    @Builder
    private PostDetailResponse(Long postId, Long userId, LocalDateTime createTime, String nickname, String content, String title,  List<String> imgList, List<Comment> commentList, Long categoryId, String categoryname) {
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.title = title;
        this.createTime = createTime;
        if(imgList.isEmpty()) {
            this.imgList = Collections.singletonList("https://wheretogo123.s3.ap-northeast-2.amazonaws.com/none.png");
        } else {
            this.imgList = imgList;
        }
        this.commentList = commentList;
        this.categoryId = categoryId;
        this.categoryName = categoryname;
    }

    public static PostDetailResponse of(Post post) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickName())
                .content(post.getContent())
                .title(post.getTitle())
                .imgList(post.getImages()
                        .stream()
                        .map(Image::getImgName) // 이미지 객체에서 이미지 이름만 추출
                        .collect(Collectors.toList()))
                .commentList(post.getComments())
                .categoryId(post.getCategory().getId())
                .categoryname(post.getCategory().getName())
                .build();
    }
}