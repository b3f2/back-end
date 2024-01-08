package com.backend.api.service.comment;

import com.backend.api.ServiceTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.post.Category;
import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.exception.CommentNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.request.comment.CommentCreateRequest;
import com.backend.api.request.comment.CommentReadRequest;
import com.backend.api.request.comment.CommentUpdateRequest;
import com.backend.api.request.post.PostCreateRequest;
import com.backend.api.response.comment.CommentResponse;
import com.backend.api.response.user.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.backend.api.config.factory.CommentCreateRequestFactory.*;
import static com.backend.api.config.factory.PostCreateRequestFactory.createPostCreateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

public class CommentServiceTest extends ServiceTestSupport {
    private final static String PROCESS_UNLIKE_COMMENT = "좋아요 취소 완료";
    private final static String PROCESS_LIKE_COMMENT = "좋아요 처리 완료";

    @AfterEach
    void teamDown() {
        commentLikeRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        imageRepository.deleteAllInBatch();
        postLikeRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    void createTest() throws IOException{
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);


        CommentCreateRequest request = new CommentCreateRequest("content", post.getId(), user.getId(), null);
        commentService.create(loginResponse, request);
        Comment parent = commentRepository.findAll().get(0);

        CommentCreateRequest children = new CommentCreateRequest("content2", post.getId(), user.getId(), parent.getId());
        commentService.create(loginResponse, children);

        //then
        assertThat(commentRepository.findAll().size()).isEqualTo(2);
        assertThat(commentRepository.findAll().get(1).getParent()).isNotNull();
        assertThat(commentRepository.findAll().get(0).getChildren()).isNotNull();
    }

    @Test
    void readAllTest() throws IOException{
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);

        CommentCreateRequest request1 = new CommentCreateRequest("content1", post.getId(), user.getId(), null);
        CommentCreateRequest request2 = new CommentCreateRequest("content2", post.getId(), user.getId(), null);

        commentService.create(loginResponse, request1);
        commentService.create(loginResponse, request2);

        //when
        CommentReadRequest commentReadRequest = new CommentReadRequest(post.getId());
        List<CommentResponse> result = commentService.readAll(commentReadRequest);

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void deleteCommentTest() throws IOException {
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);

        CommentCreateRequest request = new CommentCreateRequest("content", post.getId(), user.getId(), null);
        commentService.create(loginResponse, request);
        Comment parent = commentRepository.findAll().get(0);

        CommentCreateRequest children = new CommentCreateRequest("content2", post.getId(), user.getId(), parent.getId());
        commentService.create(loginResponse, children);

        //when
        commentService.delete(loginResponse, parent.getId());

        //then
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void updateCommentTest() throws IOException {
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);

        CommentCreateRequest request = new CommentCreateRequest("content", post.getId(), user.getId(), null);
        commentService.create(loginResponse, request);
        Comment comment = commentRepository.findAll().get(0);

        //when
        CommentUpdateRequest request1 = new CommentUpdateRequest("updated content");
        commentService.updateComment(loginResponse, comment.getId(), request1);
        Comment updatedComment = commentRepository.findAll().get(0);

       //then
       assertThat(updatedComment.getContent()).isEqualTo("updated content");
    }

    @Test
    void createExceptionByUserNotFoundTest() {
        //given
        LoginResponse loginResponse = LoginResponse.builder().build();

        //when, then
        assertThatThrownBy(()->commentService.create(loginResponse, createCommentCreateRequest()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void createExceptionByCommentNotFoundTest() throws IOException{
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);

        //when, then
        assertThatThrownBy(()->commentService.create(loginResponse, createCommentCreateRequestWithParentId(post.getId(), 1L)))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void likeCommentTest() throws IOException {
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());

        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);

        CommentCreateRequest request = new CommentCreateRequest("content", post.getId(), user.getId(), null);
        commentService.create(loginResponse, request);
        Comment comment = commentRepository.findAll().get(0);

        //when
        String result = commentService.likeComment(comment.getId(), loginResponse);

        //then
        assertThat(result).isEqualTo(PROCESS_LIKE_COMMENT);
    }

    @Test
    void unlikeCommentTest() throws IOException {
        //given
        LoginResponse loginResponse = createLogin();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .id(1L)
                .build());
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        postService.create(loginResponse, req, List.of());
        Post post = postRepository.findAll().get(0);
        CommentCreateRequest request = new CommentCreateRequest("content", post.getId(), user.getId(), null);

        commentService.create(loginResponse, request);
        Comment comment = commentRepository.findAll().get(0);
        String result = commentService.likeComment(comment.getId(), loginResponse);

        //when
        String result2 = commentService.updateCommentLike(comment.getId(), loginResponse);

        //then
        assertThat(result2).isEqualTo(PROCESS_UNLIKE_COMMENT);
    }

    private LoginResponse createLogin() {
        return LoginResponse.builder()
                .email("user@gmail.com")
                .role(Role.ROLE_USER)
                .build();
    }

}
