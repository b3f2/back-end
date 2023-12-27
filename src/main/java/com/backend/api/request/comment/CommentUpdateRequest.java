package com.backend.api.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateRequest {
    @NotBlank(message = "{commentCreateRequest.content.notBlank}")
    private String content;
}
