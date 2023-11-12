package com.backend.api.request.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCourse {

    @NotBlank(message = "코스명을 입력해주세요")
    private String name;

    @Builder
    public CreateCourse(String name) {
        this.name = name;
    }
}
