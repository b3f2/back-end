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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private Gender gender;

}
