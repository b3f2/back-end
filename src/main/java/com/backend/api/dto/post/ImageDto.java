package com.backend.api.dto.post;

import com.backend.api.entity.post.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ImageDto {
    private String imageName;
    private String imgUrl;
}
