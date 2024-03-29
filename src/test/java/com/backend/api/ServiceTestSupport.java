package com.backend.api;

import com.backend.api.jwt.TokenProvider;
import com.backend.api.repository.category.CategoryRepository;
import com.backend.api.repository.comment.CommentLikeRepository;
import com.backend.api.repository.comment.CommentRepository;
import com.backend.api.repository.course.CourseLocalRepository;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.course.CourseReviewRepository;
import com.backend.api.repository.local.FavoritesRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.local.LocalReviewRepository;
import com.backend.api.repository.post.ImageRepository;
import com.backend.api.repository.post.PostLikeRepository;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.service.Comment.CommentService;
import com.backend.api.service.auth.AuthService;
import com.backend.api.service.category.CategoryService;
import com.backend.api.service.course.CourseLocalService;
import com.backend.api.service.course.CourseReviewService;
import com.backend.api.service.course.CourseService;
import com.backend.api.service.local.FavoriteService;
import com.backend.api.service.local.LocalReviewService;
import com.backend.api.service.local.LocalService;
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
    protected PostRepository postRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

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
    protected LocalService localService;

    @Autowired
    protected LocalRepository localRepository;

    @Autowired
    protected LocalReviewService localReviewService;

    @Autowired
    protected LocalReviewRepository localReviewRepository;

    @Autowired
    protected FavoriteService favoriteService;

    @Autowired
    protected FavoritesRepository favoritesRepository;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected CommentLikeRepository commentLikeRepository;

    @Autowired
    protected CourseLocalService courseLocalService;

    @Autowired
    protected CourseLocalRepository courseLocalRepository;

    @Autowired
    protected CategoryService categoryService;


}
