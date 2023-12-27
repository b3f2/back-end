package com.backend.api.entity.post;

import com.backend.api.dto.post.UserDto;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.BaseEntity;
import com.backend.api.event.CommentCreateEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.context.ApplicationEventPublisher;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    private int liked;

    @Builder
    public Comment(String content, User user, Post post, Comment parent) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.parent = parent;
        this.liked = 0;
    }

    public void publishCreateEvent(ApplicationEventPublisher publisher) {
        publisher.publishEvent(
                new CommentCreateEvent(
                        UserDto.toDto(getUser()),
                        UserDto.toDto(getPost().getUser()),
                        Optional.ofNullable(getParent()).map(p->p.getUser()).map(m->UserDto.toDto(m)).orElseGet(()->UserDto.empty()),
                        getContent()
                )
        );
    }

    public void increaseLikeCount() {
        this.liked ++;
    }

    public void decreaseLikeCount() {
        this.liked --;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
