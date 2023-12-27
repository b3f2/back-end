package com.backend.api.request.local;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFavorite {

    @NotBlank(message = "폴더명을 입력해 주세요")
    private String name;

    @Builder
    public CreateFavorite(String name) {
        this.name = name;
    }
}
