package com.backend.api.service.user;

import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseReview;
import com.backend.api.entity.local.Favorites;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.post.Comment;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidSignUpException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.course.CourseReviewRepository;
import com.backend.api.repository.local.FavoritesRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.post.CommentRepository;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PostRepository postRepository;

    private final CourseRepository courseRepository;

    private final CourseReviewRepository courseReviewRepository;

    private final CommentRepository commentRepository;

    private final FavoritesRepository favoritesRepository;

    private final LocalRepository localRepository;

    @Transactional
    public UserResponse signup(JoinRequest request) {

         if (userRepository.existsByEmail(request.getEmail())) {
             throw new InvalidSignUpException();
         }

        String password = passwordEncoder.encode(request.getPassword());

        User user = userRepository.save(User.toEntity(request, password));

        return UserResponse.of(user);
    }

    public UserResponse get(LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse edit(LoginResponse loginResponse, UserEdit userEdit) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (userEdit.getPassword() != null) {
             userEdit.encode(passwordEncoder.encode(userEdit.getPassword()));
        }

        user.edit(userEdit);

        return UserResponse.of(user);
    }

    @Transactional
    public void delete(LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        List<Post> post = postRepository.findByUser(user);

        List<Course> course = courseRepository.findByUser(user);

        List<CourseReview> courseReview = courseReviewRepository.findByUser(user);

        List<Comment> comment = commentRepository.findByUser(user);

        List<Favorites> favorites = favoritesRepository.findByUser(user);

        List<Local> locals = localRepository.findByUser(user);

        if (!post.isEmpty()) postRepository.deleteAll(post);
        if (!course.isEmpty()) courseRepository.deleteAll(course);
        if (!courseReview.isEmpty()) courseReviewRepository.deleteAll(courseReview);
        if (!comment.isEmpty()) commentRepository.deleteAll(comment);
        if (!favorites.isEmpty()) favoritesRepository.deleteAll(favorites);
        if (!locals.isEmpty()) localRepository.deleteAll(locals);
        userRepository.delete(user);

    }

}
