package com.backend.api.entity.user;

import com.backend.api.entity.util.Address;
import com.backend.api.entity.util.BaseEntity;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Embedded
    private Address address;

    private String birth;

    private Gender gender;

}
