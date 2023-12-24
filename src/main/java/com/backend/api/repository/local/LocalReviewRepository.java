package com.backend.api.repository.local;

import com.backend.api.entity.local.LocalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalReviewRepository extends JpaRepository<LocalReview, Long> {
    List<LocalReview> findByUserId(Long userId);
}
