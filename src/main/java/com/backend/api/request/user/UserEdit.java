package com.backend.api.request.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEdit {

    String password;

    String nickName;

    String city;

    String street;

    String zipcode;


    @Builder
    private UserEdit(String password, String nickName, String city, String street, String zipcode) {
        this.password = password;
        this.nickName = nickName;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void encode(String password) {
        this.password = password;
    }
}
