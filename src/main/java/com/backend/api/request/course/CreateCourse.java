package com.backend.api.request.course;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCourse {

    @NotBlank(message = "코스명을 입력해 주세요")
    private String name;

    @Builder
    public CreateCourse(String name) {
        this.name = name;
    }
}
