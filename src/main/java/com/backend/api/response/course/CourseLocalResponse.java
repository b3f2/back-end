package com.backend.api.response.course;

import com.backend.api.entity.course.CourseLocal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseLocalResponse {

    private Long id;

    private Long courseId;

    private Long localId;

    private final int turn;

    @Builder
    public CourseLocalResponse(Long id, Long courseId, Long localId, int turn) {
        this.id = id;
        this.courseId = courseId;
        this.localId = localId;
        this.turn = turn;
    }

    public CourseLocalResponse(CourseLocal courseLocal) {
        this.id = courseLocal.getId();
        this.localId = courseLocal.getLocal().getId();
        this.courseId = courseLocal.getCourse().getId();
        this.turn = courseLocal.getTurn();
    }

    public static CourseLocalResponse of(CourseLocal courseLocal) {
        return CourseLocalResponse.builder()
                .id(courseLocal.getId())
                .localId(courseLocal.getLocal().getId())
                .courseId(courseLocal.getCourse().getId())
                .turn(courseLocal.getTurn())
                .build();
    }
}
