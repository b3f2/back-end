package com.backend.api.repository.local;

import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByUser(User user);
}
