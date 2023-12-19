package com.backend.api.repository.post;

import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p from Post p join fetch p.user where p.id=:id")
    Optional<Post> findByIdWithUser(Long id);

    Optional<Post> findPostByUser(User user);

    List<Post> findByUser(User user);

//    Optional<Post> findByTitleContaining(String keyword, Pageable pageable);
}