package com.backend.api.repository.post;

import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.CommentLike;
import com.backend.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByComment(long commentid);

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
