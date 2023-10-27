package com.backend.api.entity.course;

import com.backend.api.entity.local.Local;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class CourseLocal {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int turn;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Course course;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Local local;

}
