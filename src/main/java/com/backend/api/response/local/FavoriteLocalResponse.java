package com.backend.api.response.local;

import com.backend.api.entity.local.FavoriteLocal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteLocalResponse {

    private Long id;

    private Long favoriteId;

    private Long localId;

    @Builder
    public FavoriteLocalResponse(Long id, Long favoriteId, Long localId) {
        this.id = id;
        this.favoriteId = favoriteId;
        this.localId = localId;
    }

    public FavoriteLocalResponse(FavoriteLocal favoriteLocal) {
        this.id = favoriteLocal.getId();
        this.favoriteId = favoriteLocal.getFavorites().getId();
        this.localId = favoriteLocal.getLocal().getId();
    }

    public static FavoriteLocalResponse of(FavoriteLocal favoriteLocal) {
        return FavoriteLocalResponse.builder()
                .id(favoriteLocal.getId())
                .favoriteId(favoriteLocal.getFavorites().getId())
                .localId(favoriteLocal.getLocal().getId())
                .build();
    }
}
