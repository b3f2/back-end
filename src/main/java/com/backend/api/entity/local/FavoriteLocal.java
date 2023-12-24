package com.backend.api.entity.local;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteLocal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "favorite_id")
    private Favorites favorites;

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "local_id")
    private Local local;

    @Builder
    public FavoriteLocal(Favorites favorites, Local local) {
        setFavorites(favorites);
        setLocal(local);
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
        favorites.getFavoriteLocals().add(this);
    }

    public void setLocal(Local local) {
        this.local = local;
        local.getFavoriteLocals().add(this);
    }
}
