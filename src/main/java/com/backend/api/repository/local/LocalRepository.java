package com.backend.api.repository.local;

import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Long> {
    List<Local> findByUser(User user);
}
