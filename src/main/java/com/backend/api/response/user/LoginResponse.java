package com.backend.api.response.user;

import com.backend.api.entity.user.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String email;

    private Role role;

    @Builder
    private LoginResponse(String email, Role role) {
        this.email = email;
        this.role = role;
    }
}
