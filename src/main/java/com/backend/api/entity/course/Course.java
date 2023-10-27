package com.backend.api.entity.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Course {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

}
