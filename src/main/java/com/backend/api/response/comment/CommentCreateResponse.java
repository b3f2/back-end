package com.backend.api.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentCreateResponse {
    private Long id;
}
