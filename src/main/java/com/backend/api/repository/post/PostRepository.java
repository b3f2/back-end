package com.backend.api.repository.post;

import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
