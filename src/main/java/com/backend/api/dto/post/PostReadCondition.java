package com.backend.api.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
public class PostReadCondition {

    private static final int MAX_SIZE = 1000;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

    @Builder
    public PostReadCondition(int page, int size) {
        this.page = page;
        this.size = size;
    }
}