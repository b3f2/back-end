package com.backend.api.controller.course;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseReview;
import com.backend.api.entity.user.User;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.request.course.CreateCourseReview;
import com.backend.api.request.course.UpdateCourseReview;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class CourseReviewControllerTest extends ControllerTestSupport {

    @AfterEach
    void setUp() {
        courseReviewRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("코스 리뷰 생성")
    @WithMockCustomUser
    void createCourseReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        CreateCourseReview courseReview = CreateCourseReview.builder()
                .courseId(course.getId())
                .content("코스 리뷰")
                .rating(5)
                .build();

        String json = objectMapper.writeValueAsString(courseReview);

        //expected
        mockMvc.perform(post("/api/course-review")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
        CourseReview courseReview1 = courseReviewRepository.findAll().get(0);

        assertEquals("코스 리뷰", courseReview1.getContent());
        assertEquals(1L, courseReviewRepository.count());
        assertEquals(1L, userRepository.count());
        assertEquals(1L, courseRepository.count());
    }

    @Test
    @DisplayName("코스 리뷰 빈값 생성")
    @WithMockCustomUser
    void createEmptyCourseReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        CreateCourseReview courseReview = CreateCourseReview.builder()
                .courseId(course.getId())
                .content("")
                .rating(1)
                .build();

        String json = objectMapper.writeValueAsString(courseReview);

        //expected
        mockMvc.perform(post("/api/course-review")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("리뷰 내용을 입력해 주세요"))
                .andExpect(jsonPath("$.data").isEmpty());

        assertEquals(0L, courseReviewRepository.count());
    }

    @Test
    @DisplayName("코스 리뷰 단건 조회")
    @WithMockCustomUser
    void getCourseReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        CourseReview courseReview = CourseReview.builder()
                .course(course)
                .content("코스 리뷰")
                .rating(5)
                .build();

        courseReviewRepository.save(courseReview);

        //expected
        mockMvc.perform(get("/api/course-review/{id}", courseReview.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        CourseReview getCourseReview = courseReviewRepository.findAll().get(0);

        assertEquals(1, courseReviewRepository.count());
        assertEquals("코스 리뷰", getCourseReview.getContent());
        assertEquals(5, getCourseReview.getRating());
        assertEquals(1L, userRepository.count());
        assertEquals(1L, courseRepository.count());
    }

    @Test
    @DisplayName("모든 코스 리뷰 조회")
    @WithMockCustomUser
    void getCourseReviewList() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        List<CourseReview> courses = IntStream.range(1, 30)
                .mapToObj(i -> CourseReview.builder()
                        .content("리뷰 내용 " + i)
                        .rating(Math.min(i, 5))
                        .user(user)
                        .build())
                .toList();

        courseReviewRepository.saveAll(courses);

        //expected
        mockMvc.perform(get("/api/course-review"))
                .andDo(print())
                .andExpect(status().isOk());
        List<CourseReview> courseReviewList = courseReviewRepository.findAll();

        assertEquals("리뷰 내용 1", courseReviewList.get(0).getContent());
        assertEquals(1, courseReviewList.get(0).getRating());
        assertEquals("리뷰 내용 29", courseReviewList.get(28).getContent());
        assertEquals(5, courseReviewList.get(28).getRating());
        assertEquals(29L, courseReviewRepository.count());
        assertEquals(1L, userRepository.count());
        assertEquals(1L, courseRepository.count());
    }

    @Test
    @DisplayName("사용자의 코스 리뷰 조회")
    @WithMockCustomUser
    void getCourseReviewByUserId() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        List<CourseReview> courses = IntStream.range(1, 30)
                .mapToObj(i -> CourseReview.builder()
                        .content("리뷰 내용 " + i)
                        .rating(Math.min(i, 5))
                        .user(user)
                        .build())
                .toList();

        courseReviewRepository.saveAll(courses);


        //expected
        mockMvc.perform(get("/api/users/{userId}/course-review", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        List<CourseReview> courseReviewList = courseReviewRepository.findAll();

        assertEquals("리뷰 내용 1", courseReviewList.get(0).getContent());
        assertEquals(1, courseReviewList.get(0).getRating());
        assertEquals("리뷰 내용 29", courseReviewList.get(28).getContent());
        assertEquals(5, courseReviewList.get(28).getRating());
        assertEquals(29L, courseReviewRepository.count());
        assertEquals(1L, userRepository.count());
        assertEquals(1L, courseRepository.count());
    }

    @Test
    @DisplayName("없는 코스 리뷰 조회")
    void getEmptyCourseReview() throws Exception {
        //expected
        mockMvc.perform(get("/api/course-review"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("코스 리뷰 수정")
    @WithMockCustomUser
    void updateCourseReview() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com")
                .orElseThrow(UserNotFoundException::new);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        CourseReview courseReview = CourseReview.builder()
                .content("수정 전 코스 리뷰")
                .rating(0)
                .user(user)
                .course(course)
                .build();

        courseReviewRepository.save(courseReview);

        UpdateCourseReview updateCourseReview = UpdateCourseReview.builder()
                .content("수정 후 코스 리뷰")
                .rating(5)
                .courseId(course.getId())
                .build();

        String json = objectMapper.writeValueAsString(updateCourseReview);

        //expected
        mockMvc.perform(patch("/api/course-review/{id}", courseReview.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }
}