package com.backend.api.request.util;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {

    public static final int EXPIRATION_PERIOD = 604800000;
    private String header;

    private String data;

    @Builder
    private RefreshToken(String header, String data) {
        this.header = header;
        this.data = data;
    }
}
