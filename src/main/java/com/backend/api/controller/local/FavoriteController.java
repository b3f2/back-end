package com.backend.api.controller.local;

import com.backend.api.controller.ApiResponse;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import com.backend.api.response.local.FavoriteLocalNameResponse;
import com.backend.api.response.local.FavoriteLocalResponse;
import com.backend.api.response.local.FavoriteNameResponse;
import com.backend.api.response.local.FavoriteResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.local.FavoriteService;
import com.backend.api.util.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ApiResponse<FavoriteNameResponse> createFavorite(@Login LoginResponse loginResponse, @RequestBody @Valid CreateFavorite request) {
        return ApiResponse.ok(favoriteService.createFavorite(loginResponse, request));
    }

    @PostMapping("/favorites/{favoriteId}/local")
    public ApiResponse<FavoriteLocalResponse> addLocalToFavorite(@Login LoginResponse loginResponse, @PathVariable Long favoriteId, @RequestBody @Valid AddLocalToFavorite request) {
        return ApiResponse.ok(favoriteService.addLocalToFavorites(loginResponse, favoriteId, request));
    }

    @GetMapping("/favorites/{favoriteId}")
    public ApiResponse<FavoriteResponse> getFavorites(@PathVariable Long favoriteId) {
        return ApiResponse.ok(favoriteService.getFavorite(favoriteId));
    }

    @PatchMapping("/favorites/{favoriteId}")
    public ApiResponse<FavoriteNameResponse> updateFavoriteName(@Login LoginResponse loginResponse, @PathVariable Long favoriteId, @RequestBody @Valid UpdateFavoriteName request) {
        return ApiResponse.ok(favoriteService.updateFavoriteName(loginResponse, favoriteId, request));
    }

    @DeleteMapping("/favorites/{favoritesId}/local")
    public ApiResponse<FavoriteResponse> removeLocalToFavorites(@Login LoginResponse loginResponse, @PathVariable Long favoritesId, @RequestBody @Valid DeleteLocalToFavorite request) {
        favoriteService.deleteLocalToFavorites(loginResponse, favoritesId, request);
        return ApiResponse.<FavoriteResponse>builder()
                .status(HttpStatus.OK)
                .message("즐겨찾기 장소 제거 성공")
                .build();
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ApiResponse<FavoriteResponse> deleteFavorites(@Login LoginResponse loginResponse, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(loginResponse, favoriteId);
        return ApiResponse.<FavoriteResponse>builder()
                .status(HttpStatus.OK)
                .message("폴더 제거 성공")
                .build();
    }

    @GetMapping("/user/{userId}/favorites")
    public ApiResponse<List<FavoriteLocalNameResponse>> getFavoriteByUserId(@PathVariable Long userId) {
        return ApiResponse.ok(favoriteService.getFavoritesByUser(userId));
    }
}
