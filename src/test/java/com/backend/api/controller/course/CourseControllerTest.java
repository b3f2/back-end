package com.backend.api.controller.course;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.request.course.CreateCourse;
import com.backend.api.request.course.UpdateCourse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @AfterEach
    void setUp() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
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

    @Test
    @DisplayName("코스 생성")
    @WithMockCustomUser
    void createCourse() throws Exception {
        //given
        CreateCourse course = CreateCourse.builder()
                .name("코스")
                .build();

        String json = objectMapper.writeValueAsString(course);

        //expected
        mockMvc.perform(post("/api/courses")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(1L, courseRepository.count());
        Course course1 = courseRepository.findAll().get(0);
        assertEquals("코스", course1.getName());

    }

    @Test
    @DisplayName("코스 빈값 생성")
    @WithMockCustomUser
    void createEmptyCourse() throws Exception {
        //given
        CreateCourse course = CreateCourse.builder()
                .name("")
                .build();

        String json = objectMapper.writeValueAsString(course);

        //expected
        mockMvc.perform(post("/api/courses")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("코스명을 입력해주세요"))
                .andExpect(jsonPath("$.data").isEmpty());

        assertEquals(0L, courseRepository.count());
    }

    @Test
    @DisplayName("코스 단건 조회")
    void getCourse() throws Exception {
        //given
        User user = userCreate();

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();
        courseRepository.save(course);

        //expected
        mockMvc.perform(get("/api/courses/{id}", course.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("코스 1"));

    }

    @Test
    @DisplayName("없는 코스 조회")
    void getEmptyCourse() throws Exception {
        //expected
        mockMvc.perform(get("/api/courses/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"));

    }

    @Test
    @DisplayName("코스 목록 조회")
    void getList() throws Exception {
        //given
        User user = userCreate();

        List<Course> courses = IntStream.range(1, 30)
                .mapToObj(i -> Course.builder()
                        .name("코스 " + i)
                        .user(user)
                        .build())
                .toList();
        courseRepository.saveAll(courses);

        //expected
        mockMvc.perform(get("/api/courses")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("코스 1"))
                .andExpect(jsonPath("$.data[28].name").value("코스 29"));
    }

    @Test
    @DisplayName("없는 코스 목록 조회")
    void getEmptyList() throws Exception {
        //expected
        mockMvc.perform(get("/api/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("코스 수정")
    @WithMockCustomUser
    void updateCourse() throws Exception {
        //given
        User user = userCreate();

        Course course = Course.builder()
                .name("수정 전 코스")
                .user(user)
                .build();

        courseRepository.save(course);

        UpdateCourse updateCourse = UpdateCourse.builder()
                .name("수정된 코스")
                .build();

        String json = objectMapper.writeValueAsString(updateCourse);

        //expected
        mockMvc.perform(patch("/api/courses/{id}", course.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 코스 수정")
    @WithMockCustomUser
    void updateEmptyCourse() throws Exception {
        //given
        UpdateCourse updateCourse = UpdateCourse.builder()
                .name("수정된 코스")
                .build();

        String json = objectMapper.writeValueAsString(updateCourse);

        //expected
        mockMvc.perform(patch("/api/courses/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("해당 코스가 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("다른 사용자가 수정")
    @WithMockCustomUser
    void updateAnotherCourse() throws Exception {
        //given
        //WithMockCustomUser 사용자의 이메일은 user@gmail.com
        User anotherUser = User.builder()
                .email("another@gmail.com")
                .password(passwordEncoder.encode("1111"))
                .nickName("another")
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .birth("20001105")
                .gender(Gender.MAN)
                .build();

        Course course = Course.builder()
                .name("수정 전 코스")
                .user(anotherUser)
                .build();

        courseRepository.save(course);

        UpdateCourse updateCourse = UpdateCourse.builder()
                .name("수정된 코스")
                .build();

        String json = objectMapper.writeValueAsString(updateCourse);

        //expected
        //anotherUser의 코스를 user@gmail 사용자가 수정을 시도하는 것이다.
        mockMvc.perform(patch("/api/courses/{id}", course.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 사용자입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("코스 삭제")
    @WithMockCustomUser
    void deleteCourse() throws Exception {
        //given
        User user = userCreate();

        Course course = Course.builder()
                .name("수정 전 코스")
                .user(user)
                .build();

        courseRepository.save(course);

        //expected
        mockMvc.perform(delete("/api/courses/{id}", course.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 코스 삭제")
    @WithMockCustomUser
    void deleteEmptyCourse() throws Exception {

        //expected
        mockMvc.perform(delete("/api/courses/{id}", 1L)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("해당 코스가 없습니다."));
    }
}