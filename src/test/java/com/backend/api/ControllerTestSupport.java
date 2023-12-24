package com.backend.api;

import com.backend.api.repository.post.CategoryRepository;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.course.CourseReviewRepository;
import com.backend.api.repository.local.FavoriteLocalRepository;
import com.backend.api.repository.local.FavoritesRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.local.LocalReviewRepository;
import com.backend.api.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestSupport {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CourseRepository courseRepository;

    @Autowired
    protected CourseReviewRepository courseReviewRepository;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected CategoryRepository categoryRepository;


    @Autowired
    protected LocalRepository localRepository;

    @Autowired
    protected LocalReviewRepository localReviewRepository;

    @Autowired
    protected FavoritesRepository favoritesRepository;

    @Autowired
    protected FavoriteLocalRepository favoriteLocalRepository;
}
