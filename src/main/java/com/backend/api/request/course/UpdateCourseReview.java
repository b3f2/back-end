package com.backend.api.request.course;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCourseReview {

    @NotBlank(message = "리뷰 내용을 입력해 주세요")
    private String content;

    private int rating;

    private Long courseId;

    @Builder
    public UpdateCourseReview(String content, int rating, Long courseId) {
        this.content = content;
        this.rating = rating;
        this.courseId = courseId;
    }
}
