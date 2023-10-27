package com.backend.api.entity.local;

import com.backend.api.entity.util.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Local {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private AreaCategory areaCategory;

}
