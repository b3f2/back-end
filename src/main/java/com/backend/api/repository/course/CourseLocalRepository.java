package com.backend.api.repository.course;

import com.backend.api.entity.course.CourseLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseLocalRepository extends JpaRepository<CourseLocal, Long> {
    Optional<CourseLocal> findByCourseIdAndLocalId(Long courseId, Long localId);

    List<CourseLocal> findByCourseIdOrderByTurn(Long courseId);
    List<CourseLocal> findByCourseId(Long courseId);
}
