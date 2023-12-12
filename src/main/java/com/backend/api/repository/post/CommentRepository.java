package com.backend.api.repository.post;

import com.backend.api.entity.post.Comment;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);
}
