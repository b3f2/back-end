package com.backend.api.response.local;

import com.backend.api.entity.local.Favorites;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FavoriteResponse {

    private String name;

    private List<LocalResponse> local;

    @Builder
    public FavoriteResponse(String name, List<LocalResponse> local) {
        this.name = name;
        this.local = local;
    }

    public FavoriteResponse(Favorites favorites, List<LocalResponse> local) {
        this.name = favorites.getName();
        this.local = local;
    }


    public static FavoriteResponse of(Favorites favorites, List<LocalResponse> local) {
        return FavoriteResponse.builder()
                .name(favorites.getName())
                .local(local)
                .build();
    }
}
