package com.backend.api.entity.local;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AreaCategory {

    CAFE("카페"),
    RESTAURANT("식당"),
    CULTURE("문화"),
    ETC("기타");

    private final String text;

}
