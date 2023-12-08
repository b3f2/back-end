package com.backend.api.service.oauth2;

import com.backend.api.request.oauth2.Oauth2Request;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    KAKAO("kakao", (attributes) -> {
        Long kakao_id = (Long) attributes.get("id");
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakao_nickName = (Map<String, Object>) kakao_account.get("profile");
        return new Oauth2Request(
                String.valueOf(kakao_id),
                (String) kakao_nickName.get("nickname"),
                (String) kakao_account.get("email")
        );
    }),

    NAVER("naver", (attributes) -> {
        Map<String, Object> naver_response = (Map<String, Object>) attributes.get("response");
        return new Oauth2Request(
                (String) naver_response.get("id"),
                (String) naver_response.get("name"),
                (String) naver_response.get("email")
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, Oauth2Request> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, Oauth2Request> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    /**
     * Oauth 에 담겨있는 정보로 Oauth 회원 정보 저장하기
     */
    public static Oauth2Request extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
