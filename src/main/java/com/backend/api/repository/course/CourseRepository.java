package com.backend.api.repository.course;

import com.backend.api.entity.course.Course;
import com.backend.api.response.course.CourseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<CourseResponse> findByUserId(Long userId);
}
