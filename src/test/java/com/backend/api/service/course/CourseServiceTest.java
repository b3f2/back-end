package com.backend.api.service.course;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.course.CreateCourse;
import com.backend.api.request.course.UpdateCourse;
import com.backend.api.response.course.CourseResponse;
import com.backend.api.response.user.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CourseServiceTest extends ServiceTestSupport {

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

    private LoginResponse loginCreate(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Test
    @DisplayName("코스 생성")
    void createCourse() {
        //given
        CreateCourse course = CreateCourse.builder()
                .name("코스 1")
                .build();

        User user = userCreate();
        userRepository.save(user);
        LoginResponse loginResponse = loginCreate(user);

        //when
        courseService.createCourse(course, loginResponse);

        //then
        assertEquals(1, courseRepository.count());
        assertEquals("코스 1", courseRepository.findAll().get(0).getName());

    }

    @Test
    @DisplayName("코스 단건 조회")
    void getCourse() throws Exception {
        //given
        User user = userCreate();
        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        courseRepository.save(course);

        //when
        CourseResponse courseResponse = courseService.getCourse(course.getId());

        //then
        assertEquals(1, courseRepository.count());
        assertEquals("코스 1", courseResponse.getName());

    }

    @Test
    @DisplayName("코스 여러개 조회")
    void getList() throws Exception {
        //given
        List<Course> Courses = IntStream.range(1, 30)
                .mapToObj(i -> Course.builder()
                        .name("코스 " + i)
                        .build())
                .toList();

        courseRepository.saveAll(Courses);

        //when
        List<CourseResponse> getLists = courseService.getList();

        //then
        assertEquals(courseRepository.count(), getLists.size());
        assertEquals("코스 1", getLists.get(0).getName());
    }

    @Test
    @DisplayName("코스 수정")
    void updateCourse() throws Exception {
        //given
        User user = userCreate();
        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .user(user)
                .name("코스 1")
                .build();
        courseRepository.save(course);

        UpdateCourse updateCourse = UpdateCourse.builder()
                .name("코스 2")
                .build();


        //when
        courseService.updateCourse(loginResponse, course.getId(), updateCourse);

        //then
        Course updatedCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 코스가 없습니다. id=" + 1L));

        assertEquals(1, courseRepository.count());
        assertEquals("코스 2", courseRepository.findAll().get(0).getName());
    }

    @Test
    @DisplayName("코스 삭제")
    void deleteCourse() throws Exception {
        //given

        User user = userCreate();
        LoginResponse loginResponse = loginCreate(user);

        Course course = Course.builder()
                .name("코스 1")
                .user(user)
                .build();

        courseRepository.save(course);

        //when
        courseService.deleteCourse(loginResponse,course.getId());

        //then
        assertEquals(0, courseRepository.count());
    }
}