package com.backend.api;

import com.backend.api.jwt.TokenProvider;
import com.backend.api.repository.post.*;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.course.CourseReviewRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.service.auth.AuthService;
import com.backend.api.service.course.CourseReviewService;
import com.backend.api.service.course.CourseService;
import com.backend.api.service.file.FileService;
import com.backend.api.service.Comment.CommentService;
import com.backend.api.service.post.PostService;
import com.backend.api.service.user.UserService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public abstract class ServiceTestSupport {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Mock
    protected TokenProvider tokenProvider;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostRepository postRepository;;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected PostLikeRepository postLikeRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    protected ApplicationEventPublisher publisher;

    @Autowired
    protected CourseService courseService;

    @Autowired
    protected CourseRepository courseRepository;

    @Autowired
    protected CourseReviewService courseReviewService;

    @Autowired
    protected CourseReviewRepository courseReviewRepository;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected CommentLikeRepository commentLikeRepository;
}
