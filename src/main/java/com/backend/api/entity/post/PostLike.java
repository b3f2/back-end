package com.backend.api.entity.post;

import com.backend.api.entity.user.User;
import com.backend.api.entity.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class PostLike extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Post post;

    //true : like, false : nope
    private boolean status;

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        this.status = true;
    }
}
