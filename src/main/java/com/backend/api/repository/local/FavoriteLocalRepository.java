package com.backend.api.repository.local;

import com.backend.api.entity.local.FavoriteLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteLocalRepository extends JpaRepository<FavoriteLocal, Long> {
    Optional<FavoriteLocal> findByFavoritesIdAndLocalId(Long favoritesId, Long localId);
    List<FavoriteLocal> findByFavoritesId(Long favoritesId);
}
