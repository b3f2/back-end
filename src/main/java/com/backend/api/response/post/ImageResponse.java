package com.backend.api.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
    private Long id;
    private String originName;
    private String uniqueName;
    private Long postId;
}
