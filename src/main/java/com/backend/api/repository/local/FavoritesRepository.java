package com.backend.api.repository.local;

import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findByUser(User user);

    List<Favorites> findByUserId(Long userId);

}
