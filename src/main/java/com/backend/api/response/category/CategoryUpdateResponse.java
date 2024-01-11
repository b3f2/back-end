package com.backend.api.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryUpdateResponse {
    private Long id;
}
