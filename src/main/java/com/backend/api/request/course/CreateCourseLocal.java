package com.backend.api.request.course;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCourseLocal {

    @NotNull(message = "장소 Id를 입력해 주세요")
    private Long localId;

    @NotNull(message = "코스 Id를 입력해 주세요")
    private Long courseId;

    @Builder
    public CreateCourseLocal(Long localId, Long courseId) {
        this.localId = localId;
        this.courseId = courseId;
    }
}
