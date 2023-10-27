package com.backend.api.entity.local;

import com.backend.api.entity.user.User;
import jakarta.persistence.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
public class Favorites {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Local local;

}
