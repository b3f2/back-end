package com.backend.api.request.user;

import com.backend.api.entity.user.Gender;
import com.backend.api.entity.util.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Size(min = 8, max = 16)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Size(min = 2, max = 6)
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;

    @NotBlank(message = "주소를 입력해주세요.")
    private String city;

    @NotBlank(message = "주소를 입력해주세요.")
    private String street;

    @NotBlank(message = "주소를 입력해주세요.")
    private String zipcode;

    @Size(min = 8, max = 8)
    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birth;

    @NotBlank(message = "성별을 선택해주세요.")
    private String gender;

    public JoinRequest() {
    }

    @Builder
    private JoinRequest(String email, String password, String nickName, String city, String street, String zipcode, String birth, String gender) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.birth = birth;
        this.gender = gender;
    }
}
