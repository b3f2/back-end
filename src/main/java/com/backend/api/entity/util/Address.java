package com.backend.api.entity.util;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
public class Address {

    private String city;

    private String street;

    private String zipcode;

}
