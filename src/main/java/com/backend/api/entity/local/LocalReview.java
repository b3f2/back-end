package com.backend.api.entity.local;

import com.backend.api.entity.user.User;
import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class LocalReview {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false)
    private int rating;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Local local;
}
