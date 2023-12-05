package com.backend.api.entity.course;

import com.backend.api.entity.local.Local;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class CourseLocal {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int turn;

    @ManyToOne(fetch = LAZY)
    private Course course;

    @ManyToOne(fetch = LAZY)
    private Local local;

}
