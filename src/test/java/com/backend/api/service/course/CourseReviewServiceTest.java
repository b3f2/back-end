package com.backend.api.service.course;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseReview;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.exception.CourseReviewNotFoundException;
import com.backend.api.request.course.CreateCourseReview;
import com.backend.api.request.course.UpdateCourseReview;
import com.backend.api.response.course.CourseReviewResponse;
import com.backend.api.response.user.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseReviewServiceTest extends ServiceTestSupport {

    @AfterEach
    void setUp() {
        courseReviewRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    private User userCreate() {
        return User.builder()
                .email("user@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("user")
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .birth("20001105")
                .gender(Gender.MAN)
                .build();
    }

    private LoginResponse loginCreate(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Test
    @DisplayName("코스 리뷰 생성")
    void createCourseReview() {
        //given
        User user = userCreate();

        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        //when
        courseRepository.save(course);

        CreateCourseReview courseReview = CreateCourseReview.builder()
                .content("코스 리뷰 1")
                .rating(5)
                .courseId(course.getId())
                .build();

        courseReviewService.createCourseReview(courseReview, loginResponse);

        //then
        assertEquals(1, courseReviewRepository.count());
        assertEquals("코스 리뷰 1", courseReviewRepository.findAll().get(0).getContent());
        assertEquals(5, courseReviewRepository.findAll().get(0).getRating());

    }

    @Test
    @DisplayName("코스 리뷰 조회")
    void getCourse() throws Exception {
        //given
        User user = userCreate();

        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        //when
        courseRepository.save(course);

        CreateCourseReview courseReview = CreateCourseReview.builder()
                .content("코스 리뷰 1")
                .rating(4)
                .courseId(course.getId())
                .build();

        courseReviewService.createCourseReview(courseReview, loginResponse);

        //when
        List<CourseReviewResponse> courseReviewResponse = courseReviewService.getCourseReview(user.getId());

        //then
        assertEquals(1, courseReviewRepository.count());
        assertEquals("코스 리뷰 1", courseReviewResponse.get(0).getContent());
        assertEquals(4, courseReviewResponse.get(0).getRating());


    }

    @Test
    @DisplayName("코스 리뷰 여러개 조회")
    void getList() throws Exception {
        //given
        User user = userCreate();

        userRepository.save(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        List<CourseReview> courseReviews = IntStream.range(1, 30)
                .mapToObj(i -> CourseReview.builder()
                        .content("리뷰 내용 " + i)
                        .rating(Math.min(i, 5))
                        .user(user)
                        .build())
                .toList();

        courseReviewRepository.saveAll(courseReviews);

        //when
        List<CourseReviewResponse> getLists = courseReviewService.getList();

        //then
        assertEquals(courseRepository.count(), getLists.size(), 30);
        assertEquals("리뷰 내용 1", getLists.get(0).getContent());
        assertEquals(1, getLists.get(0).getRating());
        assertEquals("리뷰 내용 29", getLists.get(28).getContent());
        assertEquals(5, getLists.get(28).getRating());

    }

    @Test
    @DisplayName("코스 리뷰 수정")
    void updateCourse() throws Exception {
        //given
        User user = userCreate();

        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .user(user)
                .name("코스 1")
                .build();

        courseRepository.save(course);

        CourseReview courseReview = CourseReview.builder()
                .content("코스 리뷰 1")
                .rating(0)
                .course(course)
                .build();

        courseReviewRepository.save(courseReview);

        UpdateCourseReview updateCourseReview = UpdateCourseReview.builder()
                .courseId(course.getId())
                .content("수정된 코스 리뷰")
                .rating(5)
                .build();

        //when
        courseReviewService.updateCourseReview(loginResponse, courseReview.getId(), updateCourseReview);

        //then
        CourseReview updatedCourseReview = courseReviewRepository.findById(courseReview.getId())
                .orElseThrow(CourseReviewNotFoundException::new);

        assertEquals(1, courseReviewRepository.count());
        assertEquals("수정된 코스 리뷰", updatedCourseReview.getContent());
        assertEquals(5, updatedCourseReview.getRating());
    }

    @Test
    @DisplayName("코스 리뷰 삭제")
    void deleteCourse() throws Exception {
        //given
        User user = userCreate();

        userRepository.save(user);

        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        courseRepository.save(course);

        CourseReview courseReview = CourseReview.builder()
                .content("코스 리뷰 1")
                .rating(0)
                .course(course)
                .user(user)
                .build();

        courseReviewRepository.save(courseReview);

        //when
        courseReviewService.deleteCourseReview(loginResponse, courseReview.getId());

        //then
        assertEquals(0, courseReviewRepository.count());
    }
}