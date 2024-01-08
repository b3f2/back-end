package com.backend.api.entity.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
//    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Builder
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
