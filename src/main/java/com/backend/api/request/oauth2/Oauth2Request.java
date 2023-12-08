package com.backend.api.request.oauth2;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Oauth2Request {

    private String oauthId;

    private String name;

    private String email;

    @Builder
    public Oauth2Request(String oauthId, String name, String email) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
    }
}
