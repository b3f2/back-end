package com.backend.api.repository.course;

import com.backend.api.entity.course.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByUserId(Long userId);
}
