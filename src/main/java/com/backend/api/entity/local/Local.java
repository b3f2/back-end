package com.backend.api.entity.local;

import com.backend.api.entity.course.CourseLocal;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Local {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String x;

    private String y;

    @OneToMany(mappedBy = "local", fetch = LAZY)
    private List<FavoriteLocal> favoriteLocals = new ArrayList<>();

    @OneToMany(mappedBy = "local", fetch = LAZY)
    private List<CourseLocal> courseLocals = new ArrayList<>();

    @Embedded
    private Address address;

    private AreaCategory areaCategory;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Builder
    public Local(String name, String x, String y, Address address, AreaCategory areaCategory,User user) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.address = address;
        this.areaCategory = areaCategory;
        this.user = user;
    }
}
