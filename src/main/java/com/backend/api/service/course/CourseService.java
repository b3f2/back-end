package com.backend.api.service.course;

import com.backend.api.entity.course.Course;
import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidCourseException;
import com.backend.api.exception.InvalidUserException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.course.CreateCourse;
import com.backend.api.request.course.UpdateCourse;
import com.backend.api.response.course.CourseResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;


    @Transactional
    public CourseResponse createCourse(CreateCourse createCourse, LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name(createCourse.getName())
                .user(user)
                .build();

        return CourseResponse.of(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse updateCourse(LoginResponse loginResponse, Long id, UpdateCourse updateCourse) {
        Course course = courseRepository.findById(id)
                .orElseThrow(InvalidCourseException::new);

        if(!course.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        course.update(updateCourse.getName());

        return CourseResponse.of(course);
    }

    @Transactional
    public void deleteCourse(LoginResponse loginResponse, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(InvalidCourseException::new);

        if(!course.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        courseRepository.deleteById(id);
    }

    public CourseResponse getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(InvalidCourseException::new);

        return CourseResponse.builder()
                .name(course.getName())
                .build();
    }

    public List<CourseResponse> getList() {
        return courseRepository.findAll().stream()
                .map(CourseResponse::new)
                .toList();
    }

    public List<CourseResponse> getCoursesByUserId(Long userId) {
        return courseRepository.findByUserId(userId).stream()
                .map(CourseResponse::new)
                .toList();
    }
}
