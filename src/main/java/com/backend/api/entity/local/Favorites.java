package com.backend.api.entity.local;

import com.backend.api.entity.user.User;
import com.backend.api.entity.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorites extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    private User user;

    @OneToMany(mappedBy = "favorites", fetch = LAZY)
    private List<Local> Local = new ArrayList<>();

    @Builder
    public Favorites(String name, User user){
        this.name = name;
        this.user = user;
    }

    public void addLocals(Local local) {
        this.Local.add(local);
        local.setFavorites(this);
    }

    public void updateName(String name){
        this.name = name;
    }
}
