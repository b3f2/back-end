package com.backend.api.request.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCourseReview {

    @NotBlank(message = "리뷰 내용을 입력해 주세요")
    private String content;

    @Min(value = 0, message = "0~5 사이의 숫자를 입력해 주세요")
    @Max(value = 5, message = "0~5 사이의 숫자를 입력해 주세요")
    private int rating;

    private Long courseId;

    @Builder
    public CreateCourseReview(String content, int rating, Long courseId) {
        this.content = content;
        this.rating = rating;
        this.courseId = courseId;
    }
}
