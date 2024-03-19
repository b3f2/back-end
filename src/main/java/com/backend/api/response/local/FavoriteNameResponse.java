package com.backend.api.response.local;

import com.backend.api.entity.local.Favorites;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteNameResponse {
    private final String name;

    @Builder
    public FavoriteNameResponse(String name) {
        this.name = name;
    }
    public static FavoriteNameResponse of(Favorites favorites) {
        return FavoriteNameResponse.builder()
                .name(favorites.getName())
                .build();
    }
}
