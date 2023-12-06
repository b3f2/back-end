package com.backend.api.request.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthEdit {

    private String password;

    private String city;

    private String street;

    private String zipcode;

    private String birth;

    private String gender;

    @Builder
    public OauthEdit(String password, String city, String street, String zipcode,  String birth, String gender) {
        this.password = password;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.birth = birth;
        this.gender = gender;
    }
}
