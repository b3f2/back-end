package com.backend.api.service.local;

import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.exception.FavoriteNotFoundException;
import com.backend.api.exception.InvalidUserException;
import com.backend.api.exception.LocalNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.local.FavoritesRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.local.AddLocalToFavorite;
import com.backend.api.request.local.CreateFavorite;
import com.backend.api.request.local.DeleteLocalToFavorite;
import com.backend.api.request.local.UpdateFavoriteName;
import com.backend.api.response.local.FavoriteNameResponse;
import com.backend.api.response.local.FavoriteResponse;
import com.backend.api.response.local.LocalResponse;
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
    public FavoriteResponse addLocalToFavorites(LoginResponse loginResponse, Long id, AddLocalToFavorite addLocalToFavorite) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        Local local = localRepository.findById(addLocalToFavorite.getLocalId())
                .orElseThrow(LocalNotFoundException::new);

        favorites.addLocals(local);
        localRepository.save(local);

        return FavoriteResponse.of(favorites);
    }

    @Transactional
    public void deleteLocalToFavorites(LoginResponse loginResponse, Long favoritesId, DeleteLocalToFavorite request) {
        Favorites favorites = favoritesRepository.findById(favoritesId)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        favorites.getLocal().removeIf(local -> local.getId().equals(request.getLocalId()));
    }

    @Transactional
    public void deleteFavorite(LoginResponse loginResponse, Long id) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        if (!favorites.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        List<Local> locals = localRepository.findAllByFavorites(favorites);

        for (Local local : locals) {
            local.setFavorites(null);
            localRepository.save(local);
        }
        favoritesRepository.deleteById(id);
    }

    public FavoriteResponse getFavorite(Long id) {
        Favorites favorites = favoritesRepository.findById(id)
                .orElseThrow(FavoriteNotFoundException::new);

        List<LocalResponse> list = favorites.getLocal().stream()
                .map(LocalResponse::of)
                .toList();

        return FavoriteResponse.builder()
                .name(favorites.getName())
                .local(list)
                .build();
    }

    public List<FavoriteResponse> getFavoritesByUser(Long userId) {
        List<Favorites> byUserId = favoritesRepository.findByUserId(userId);
        return byUserId.stream()
                .map(FavoriteResponse::of)
                .toList();
    }

}