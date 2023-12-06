package com.backend.api.response.course;

import com.backend.api.entity.course.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseResponse {

    private final String name;

    @Builder
    public CourseResponse(String name) {
        this.name = name;
    }

    public CourseResponse(Course course) {
        this.name = course.getName();
    }

    public static CourseResponse of(Course course) {
        return CourseResponse.builder()
                .name(course.getName())
                .build();
    }
}
