package com.backend.api.response.user;

import com.backend.api.request.util.AccessToken;
import com.backend.api.request.util.RefreshToken;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {

    private AccessToken accessToken;

    private RefreshToken refreshToken;


    @Builder
    private TokenResponse(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
