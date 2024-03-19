package com.backend.api.repository.local;

import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    List<Local> findByUser(User user);

    List<Local> findByUserId(Long userId);

    List<Local> findAllByFavorites(Favorites favorites);
}
