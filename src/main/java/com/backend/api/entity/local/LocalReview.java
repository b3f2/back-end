package com.backend.api.entity.local;

import com.backend.api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalReview {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false)
    private int rating;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Local local;

    @Builder
    public LocalReview(String content, int rating, User user, Local local) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.local = local;
    }

    public void update(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }
}
