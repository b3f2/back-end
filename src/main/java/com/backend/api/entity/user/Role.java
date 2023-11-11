package com.backend.api.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("USER");

    private final String key;

}
