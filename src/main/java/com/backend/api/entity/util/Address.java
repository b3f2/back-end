package com.backend.api.entity.util;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String city;

    private String street;

    private String zipcode;

    @Builder
    private Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void edit(String city, String street, String zipcode) {
        this.city = city != null ? city : this.city;
        this.street = city != null ? street : this.street;
        this.zipcode = city != null ? zipcode : this.zipcode;
    }
}
