package com.backend.api.entity.course;

import com.backend.api.entity.user.User;
import com.backend.api.entity.util.BaseEntity;
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
public class CourseReview extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false)
    private int rating;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Course course;

    @Builder
    public CourseReview(String content, int rating, User user, Course course) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.course = course;
    }

    public void update(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }
}
