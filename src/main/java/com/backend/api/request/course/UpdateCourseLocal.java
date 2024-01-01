package com.backend.api.request.course;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCourseLocal {

    @NotNull(message = "코스 Id를 입력해 주세요")
    Long courseId;

    @NotNull(message = "장소 Id를 입력해 주세요")
    Long localId;

    @NotNull(message = "바꿀 순서를 입력해 주세요")
    int turn;

    @Builder
    public UpdateCourseLocal(Long courseId, Long localId, int turn) {
        this.courseId = courseId;
        this.localId = localId;
        this.turn = turn;
    }
}
