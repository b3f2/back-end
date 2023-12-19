package com.backend.api.dto.post;

import com.backend.api.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nickname;
    private String email;
    private String birth;

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getBirth()
        );
    }

    public static UserDto empty() {
        return new UserDto(null, "", "", "");
    }
}
