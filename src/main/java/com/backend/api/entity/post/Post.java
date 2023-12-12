package com.backend.api.entity.post;

import com.backend.api.entity.user.User;
import com.backend.api.entity.util.BaseEntity;
import com.backend.api.request.post.PostUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    private int liked;

    @Builder
    public Post(String title, String content, User user, Category category, List<Image> images) {
        this.title = title;
        this.user = user;
        this.content = content;
        this.category = category;
        this.images = images;
        for(Image img : images) {
            img.setPost(this);
        }
        this.liked = 0;
    }

    public void updatePost(PostUpdateRequest req, List<Image> images){
        this.title = req.getTitle();
        this.content = req.getContent();
        this.images  = images;
        for(Image img : images) {
            img.setPost(this);
        }
    }

    public void increaseLikeCount() {
        this.liked ++;
    }

    public void decreaseLikeCount() {
        this.liked --;
    }
}
