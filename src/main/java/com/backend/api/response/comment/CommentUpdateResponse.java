package com.backend.api.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentUpdateResponse {
    private Long id;
}
