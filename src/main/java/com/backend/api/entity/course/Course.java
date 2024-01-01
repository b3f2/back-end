package com.backend.api.entity.course;

import com.backend.api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "course", fetch = LAZY)
    private List<CourseLocal> courseLocals = new ArrayList<>();

    @Builder
    public Course(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void update(String name) {
        this.name = name;
    }
}