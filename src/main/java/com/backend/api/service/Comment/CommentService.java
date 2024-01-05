package com.backend.api.service.Comment;

import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.CommentLike;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;
import com.backend.api.exception.*;
import com.backend.api.repository.post.CommentLikeRepository;
import com.backend.api.repository.post.CommentRepository;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.comment.CommentCreateRequest;
import com.backend.api.request.comment.CommentReadRequest;
import com.backend.api.request.comment.CommentUpdateRequest;
import com.backend.api.response.comment.CommentCreateResponse;
import com.backend.api.response.comment.CommentResponse;
import com.backend.api.response.comment.CommentUpdateResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;
    private final CommentLikeRepository commentLikeRespository;

    private final static String PROCESS_UNLIKE_COMMENT = "좋아요 취소 완료";
    private final static String PROCESS_LIKE_COMMENT = "좋아요 처리 완료";

    public List<CommentResponse> readAll(CommentReadRequest cond) {
        return commentRepository.findCommentsWithChildrenOrderedByDate(cond.getPostId());
    }

    @Transactional
    public CommentCreateResponse create(LoginResponse loginResponse, CommentCreateRequest req) {
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(req.getPostId()).orElseThrow(PostNotFoundException::new);
        Comment parent = Optional.ofNullable(req.getParentId())
                .map(id-> commentRepository.findById(id).orElseThrow(CommentNotFoundException::new))
                .orElse(null);

        Comment comment = commentRepository.save(new Comment(req.getContent(), user, post, parent));

        if(parent != null) {
            parent.getChildren().add(comment);
        }

        comment.publishCreateEvent(publisher);
        return new CommentCreateResponse(comment.getId());
    }

    @Transactional
    public void delete(LoginResponse loginResponse, Long id) {
        Comment comment = commentRepository.findWithParentById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);

        //본인 post인지 확인
        if(!comment.getUser().equals(user)) {
            throw new NotYourCommentException();
        }

        //부모 댓글의 모든 자식 댓글 가져옴
        List<Comment> children = comment.getChildren();
        deleteChildren(children);

        //부모 댓글 삭제
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentUpdateResponse updateComment(LoginResponse loginResponse, Long commentId, CommentUpdateRequest request) {
        Comment comment = loadCommentByCommentId(commentId);

        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!comment.getUser().equals(user)) {
            throw new NotYourCommentException();
        }

        comment.setContent(request.getContent());
        commentRepository.save(comment);
        return new CommentUpdateResponse(comment.getId());
    }

    @Transactional
    public String likeComment(Long id, LoginResponse loginResponse) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        CommentLike commentLike = new CommentLike(comment, user);
        commentLikeRespository.save(commentLike);
        return PROCESS_LIKE_COMMENT;
    }

    @Transactional
    public String updateCommentLike(Long id, LoginResponse loginResponse) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        if(!hasLikeComment(comment, user)) {
            comment.increaseLikeCount();
            return likeComment(id, loginResponse);
        }
        comment.decreaseLikeCount();
        return removeLikeComment(comment, user);
    }

    public boolean hasLikeComment(Comment comment, User user) {
        return commentLikeRespository.findByCommentAndUser(comment, user).isPresent();
    }

    @Transactional
    public String removeLikeComment(Comment comment, User user) {
        CommentLike commentLike = commentLikeRespository.findByCommentAndUser(comment, user).orElseThrow(CommentLikeNotFoundException::new);
        commentLikeRespository.delete(commentLike);
        return PROCESS_UNLIKE_COMMENT;
    }

    @Transactional
    public void deleteChildren(List<Comment> children) {
        for(Comment child : children) {
            List<Comment> grandchildren = child.getChildren();
            if(!grandchildren.isEmpty()) {
                deleteChildren(grandchildren);
            }
            commentRepository.delete(child);
        }
    }

    private Comment loadCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

}
