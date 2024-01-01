package com.backend.api.service.local;

import com.backend.api.entity.local.FavoriteLocal;
import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.exception.*;
import com.backend.api.repository.local.FavoriteLocalRepository;
import com.backend.api.repository.local.FavoritesRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import com.backend.api.response.local.*;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final UserRepository userRepository;
    private final FavoritesRepository favoritesRepository;
    private final LocalRepository localRepository;
    private final FavoriteLocalRepository favoriteLocalRepository;

    @Transactional
    public FavoriteNameResponse createFavorite(LoginResponse loginResponse, CreateFavorite request) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Favorites favorites = Favorites.builder()
                .name(request.getName())
                .user(user)
                .build();

        return FavoriteNameResponse.of(favoritesRepository.save(favorites));
    }

    @Transactional
    public FavoriteNameResponse updateFavoriteName(LoginResponse loginResponse, Long id, UpdateFavoriteName updateFavoriteName) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        favorites.updateName(updateFavoriteName.getName());
        return FavoriteNameResponse.of(favorites);
    }

    @Transactional
    public FavoriteLocalResponse addLocalToFavorites(LoginResponse loginResponse, Long id, AddLocalToFavorite addLocalToFavorite) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        Local local = localRepository.findById(addLocalToFavorite.getLocalId())
                .orElseThrow(LocalNotFoundException::new);

        FavoriteLocal favoriteLocal = FavoriteLocal.builder()
                .favorites(favorites)
                .local(local)
                .build();

        favoriteLocalRepository.save(favoriteLocal);

        return FavoriteLocalResponse.of(favoriteLocal);
    }

    @Transactional
    public void deleteLocalToFavorites(LoginResponse loginResponse, Long favoritesId, DeleteLocalToFavorite request) {
        FavoriteLocal favoriteLocal = favoriteLocalRepository.findByFavoritesIdAndLocalId(favoritesId, request.getLocalId())
                .orElseThrow(FavoriteLocalNotFoundException::new);

        if (!favoriteLocal.getFavorites().getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        favoriteLocalRepository.delete(favoriteLocal);
    }

    @Transactional
    public void deleteFavorite(LoginResponse loginResponse, Long id) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        List<FavoriteLocal> byFavoritesId = favoriteLocalRepository.findByFavoritesId(id);
        favoriteLocalRepository.deleteAll(byFavoritesId);

        favoritesRepository.deleteById(id);
    }

    public FavoriteResponse getFavorite(Long id) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        List<LocalResponse> list = favorites.getFavoriteLocals().stream()
                .map(FavoriteLocal::getLocal)
                .map(LocalResponse::of)
                .toList();

        return FavoriteResponse.builder()
                .name(favorites.getName())
                .local(list)
                .build();
    }

    public List<FavoriteLocalNameResponse> getFavoritesByUser(Long userId) {
        return favoritesRepository.findByUserId(userId).stream()
                .map(favorites -> new FavoriteLocalNameResponse(
                        favorites.getName(),
                        favorites.getFavoriteLocals().stream()
                                .map(FavoriteLocal::getLocal)
                                .map(Local::getName)
                                .toList()))
                .toList();
    }

}