package com.backend.api.repository.post;

import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;
import com.backend.api.response.comment.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post postId);

    @Override
    void deleteById(Long commentid);

    @Override
    <S extends Comment> S save(S entity);

    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Optional<Comment> findWithParentById(@Param("id") Long id);

    @Query("select c from Comment c join fetch c.user left join fetch c.parent where c.post.id = :postId order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllWithUserAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(@Param("postId") Long postId);

    @Query("select c FROM Comment c left join fetch c.children where c.parent is null and c.post.id = :postId order by c.createTime asc, c.id asc")
    List<CommentResponse> findCommentsWithChildrenOrderedByDate(@Param("postId") Long postId);
}
