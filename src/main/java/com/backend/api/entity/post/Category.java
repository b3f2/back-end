package com.backend.api.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Category {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

}
