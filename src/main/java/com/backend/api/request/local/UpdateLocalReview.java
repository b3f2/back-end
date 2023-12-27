package com.backend.api.request.local;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateLocalReview {

    @NotBlank(message = "리뷰 내용을 입력해 주세요")
    private String content;

    @Min(value = 0, message = "0~5 사이의 숫자를 입력해 주세요")
    @Max(value = 5, message = "0~5 사이의 숫자를 입력해 주세요")
    @NotNull(message = "평점을 입력해 주세요")
    private int rating;

    @NotNull(message = "장소 Id를 입력해 주세요")
    private Long localId;

    @Builder
    public UpdateLocalReview(String content, int rating, Long localId) {
        this.content = content;
        this.rating = rating;
        this.localId = localId;
    }
}
