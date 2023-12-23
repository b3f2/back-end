package com.backend.api.request.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCourseReview {

    @NotBlank(message = "리뷰 내용을 입력해 주세요")
    private String content;

    @NotNull(message = "리뷰 점수를 입력해 주세요")
    private int rating;

    @NotNull(message = "코스 Id를 입력해 주세요")
    private Long courseId;

    @Builder
    public UpdateCourseReview(String content, int rating, Long courseId) {
        this.content = content;
        this.rating = rating;
        this.courseId = courseId;
    }
}
