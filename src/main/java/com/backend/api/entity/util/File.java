package com.backend.api.entity.util;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class File {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String originFileName;

    private String fullPath;

    

}
