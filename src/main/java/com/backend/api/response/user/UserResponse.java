package com.backend.api.response.user;

import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private Long id;

    private String email;

    private String password;

    private String nickName;

    private Address address;

    private String birth;

    private Gender gender;

    private Role role;

    private String refreshToken;

    @Builder
    private UserResponse(Long id , String email, String password, String nickName, Address address, String birth, Gender gender, Role role, String refreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.address = address;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickName(user.getNickName())
                .address(user.getAddress())
                .gender(user.getGender())
                .birth(user.getBirth())
                .role(user.getRole())
                .refreshToken(user.getRefreshToken())
                .build();
    }
}
