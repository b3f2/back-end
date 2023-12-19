package com.backend.api.entity.local;

import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Local {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private AreaCategory areaCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
