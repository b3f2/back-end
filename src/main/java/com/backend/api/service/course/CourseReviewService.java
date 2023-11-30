package com.backend.api.service.course;

import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseReview;
import com.backend.api.entity.user.User;
import com.backend.api.exception.CourseReviewNotFoundException;
import com.backend.api.exception.InvalidCourseException;
import com.backend.api.exception.InvalidUserException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.course.CourseReviewRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.course.CreateCourseReview;
import com.backend.api.request.course.UpdateCourseReview;
import com.backend.api.response.course.CourseReviewResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseReviewService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseReviewRepository courseReviewRepository;

    @Transactional
    public CourseReviewResponse createCourseReview(CreateCourseReview createCourseReview, LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Course course = courseRepository.findById(createCourseReview.getCourseId())
                .orElseThrow(InvalidCourseException::new);

        CourseReview courseReview = CourseReview.builder()
                .content(createCourseReview.getContent())
                .rating(createCourseReview.getRating())
                .course(course)
                .user(user)
                .build();

        return CourseReviewResponse.of(courseReviewRepository.save(courseReview));
    }

    @Transactional
    public CourseReviewResponse updateCourseReview(LoginResponse loginResponse, Long id, UpdateCourseReview updateCourseReview) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Course course = courseRepository.findById(updateCourseReview.getCourseId())
                .orElseThrow(InvalidCourseException::new);

        if(!course.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        CourseReview courseReview = courseReviewRepository.findById(id)
                .orElseThrow(CourseReviewNotFoundException::new);

        courseReview.update(updateCourseReview.getContent(), updateCourseReview.getRating());

        return CourseReviewResponse.of(courseReview);
    }

    @Transactional
    public void deleteCourseReview(LoginResponse loginResponse, Long id) {
        CourseReview courseReview = courseReviewRepository.findById(id)
                .orElseThrow(CourseReviewNotFoundException::new);

        if(!courseReview.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        courseReviewRepository.deleteById(id);
    }

    public List<CourseReviewResponse> getList() {
        return courseReviewRepository.findAll().stream()
                .map(CourseReviewResponse::new)
                .toList();
    }

    public List<CourseReviewResponse> getCourseReview(Long userId){
        return courseReviewRepository.findByUserId(userId).stream()
                .map(CourseReviewResponse::new)
                .toList();
    }
}
