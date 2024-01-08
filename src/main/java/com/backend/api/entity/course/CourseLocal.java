package com.backend.api.entity.course;

import com.backend.api.entity.local.Local;
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
public class CourseLocal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int turn;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "local_id")
    private Local local;

    @Builder
    public CourseLocal(Course course, Local local, int turn) {
        setCourse(course);
        setLocal(local);
        this.turn = turn;
    }

    public void setCourse(Course course) {
        this.course = course;
        course.getCourseLocals().add(this);
    }

    public void setLocal(Local local) {
        this.local = local;
        local.getCourseLocals().add(this);
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
