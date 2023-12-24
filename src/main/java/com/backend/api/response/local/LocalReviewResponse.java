package com.backend.api.response.local;

import com.backend.api.entity.local.LocalReview;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocalReviewResponse {

    private String content;

    private int rating;

    @Builder
    public LocalReviewResponse(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

    public LocalReviewResponse(LocalReview localReview) {
        this.content = localReview.getContent();
        this.rating = localReview.getRating();
    }

    public static LocalReviewResponse of(LocalReview localReview) {
        return LocalReviewResponse.builder()
                .content(localReview.getContent())
                .rating(localReview.getRating())
                .build();
    }
}
