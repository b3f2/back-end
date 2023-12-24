package com.backend.api.response.local;

import com.backend.api.entity.local.FavoriteLocal;
import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FavoriteLocalNameResponse {

    private String name;
    private List<String> localNames;

    @Builder
    public FavoriteLocalNameResponse(String name, List<String> localNames) {
        this.name = name;
        this.localNames = localNames;
    }

    public FavoriteLocalNameResponse(Favorites favorites) {
        this.name = favorites.getName();
        this.localNames = favorites.getFavoriteLocals().stream()
                .map(FavoriteLocal::getLocal)
                .map(Local::getName)
                .collect(Collectors.toList());
    }
}
