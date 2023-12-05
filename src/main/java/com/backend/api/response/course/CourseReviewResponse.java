package com.backend.api.response.course;

import com.backend.api.entity.course.CourseReview;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseReviewResponse {

    private final String content;

    private final int rating;

    @Builder
    public CourseReviewResponse(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

    public CourseReviewResponse(CourseReview courseReview) {
        this.content = courseReview.getContent();
        this.rating = courseReview.getRating();
    }

    public static CourseReviewResponse of(CourseReview courseReview) {
        return CourseReviewResponse.builder()
                .content(courseReview.getContent())
                .rating(courseReview.getRating())
                .build();
    }
}
