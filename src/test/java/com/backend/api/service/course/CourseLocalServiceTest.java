package com.backend.api.service.course;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.course.CreateCourseLocal;
import com.backend.api.request.course.UpdateCourseLocal;
import com.backend.api.request.local.AddLocalToCourse;
import com.backend.api.response.course.CourseLocalResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class CourseLocalServiceTest extends ServiceTestSupport {

    @AfterEach
    void setUp() {
        courseLocalRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        localRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("코스 안에 장소 넣기")
    @Transactional
    void createCourseLocal() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Course course = Course.builder()
                .name("코스")
                .user(user)
                .build();
        courseRepository.save(course);

        Local local = localRepository.save(localCreate(user));

        //when
        CreateCourseLocal createCourseLocal = CreateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local.getId())
                .build();

        courseLocalService.createCourseLocal(loginCreate(user), createCourseLocal);

        //then
        assertEquals(1, courseLocalRepository.count());
        assertEquals("코스", courseLocalRepository.findAll().get(0).getCourse().getName());
        assertEquals("user@gmail.com", courseLocalRepository.findAll().get(0).getLocal().getUser().getEmail());
    }

    @Test
    @DisplayName("코스 안에 장소 추가하기")
    @Transactional
    void addLocalToCourse() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Course course = Course.builder()
                .name("코스")
                .user(user)
                .build();
        courseRepository.save(course);

        Local local1 = localRepository.save(localCreate(user));

        CreateCourseLocal createCourseLocal = CreateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local1.getId())
                .build();

        courseLocalService.createCourseLocal(loginCreate(user), createCourseLocal);

        //when
        Local local2 = Local.builder()
                .x(String.valueOf(12.4321))
                .y(String.valueOf(121.1212))
                .address(Address.builder()
                        .city("서울시")
                        .street("아리수로 93길 9-14")
                        .zipcode("4층 열람실")
                        .build())
                .areaCategory(AreaCategory.CULTURE)
                .name("도서관")
                .user(user)
                .build();
        localRepository.save(local2);

        AddLocalToCourse addLocalToCourse = AddLocalToCourse.builder()
                .localId(local2.getId())
                .build();

        courseLocalService.addLocalToCourse(loginCreate(user), course.getId(), addLocalToCourse);

        //then
        assertEquals(1, courseRepository.count());
        assertEquals(2, courseLocalRepository.count());
        assertEquals(0, courseLocalRepository.findAll().get(0).getTurn());
        assertEquals(1, courseLocalRepository.findAll().get(1).getTurn());
        assertEquals("코스", courseLocalRepository.findAll().get(0).getCourse().getName());
        assertEquals("코스", courseLocalRepository.findAll().get(1).getCourse().getName());
        assertEquals("user@gmail.com", courseLocalRepository.findAll().get(0).getLocal().getUser().getEmail());
        assertEquals("카페 맛집", courseLocalRepository.findAll().get(0).getLocal().getName());
        assertEquals("도서관", courseLocalRepository.findAll().get(1).getLocal().getName());
    }

    @Test
    @DisplayName("코스 안에 담긴 장소 조회하기")
    @Transactional
    void getCourseLocalList() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Course course = Course.builder()
                .name("코스")
                .user(user)
                .build();
        courseRepository.save(course);

        Local local1 = localRepository.save(localCreate(user));

        CreateCourseLocal createCourseLocal = CreateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local1.getId())
                .build();

        courseLocalService.createCourseLocal(loginCreate(user), createCourseLocal);

        Local local2 = Local.builder()
                .x(String.valueOf(12.4321))
                .y(String.valueOf(121.1212))
                .address(Address.builder()
                        .city("서울시")
                        .street("아리수로 93길 9-14")
                        .zipcode("4층 열람실")
                        .build())
                .areaCategory(AreaCategory.CULTURE)
                .name("도서관")
                .user(user)
                .build();
        localRepository.save(local2);

        AddLocalToCourse addLocalToCourse = AddLocalToCourse.builder()
                .localId(local2.getId())
                .build();

        courseLocalService.addLocalToCourse(loginCreate(user), course.getId(), addLocalToCourse);
        courseLocalService.addLocalToCourse(loginCreate(user), course.getId(), addLocalToCourse);

        //when
        List<CourseLocalResponse> courseLocalList = courseLocalService.getCourseLocalList(course.getId());

        //then
        assertEquals(1, courseRepository.count());
        assertEquals(3, courseLocalRepository.count());
        assertEquals(0, courseLocalList.get(0).getTurn());
        assertEquals(1, courseLocalList.get(1).getTurn());
        assertEquals(2, courseLocalList.get(2).getTurn());
        assertEquals(course.getId(), courseLocalList.get(0).getCourseId());
        assertEquals(local1.getId(), courseLocalList.get(0).getLocalId());
        assertEquals(local2.getId(), courseLocalList.get(1).getLocalId());
        assertEquals(local2.getId(), courseLocalList.get(2).getLocalId());
    }

    @Test
    @DisplayName("코스에 담긴 장소 순서 바꾸기")
    @Transactional
    void updateCourseLocal() throws Exception {
        //given
        User user = userCreate();
        userRepository.save(user);

        Course course = Course.builder()
                .name("코스")
                .user(user)
                .build();
        courseRepository.save(course);

        Local local1 = localRepository.save(localCreate(user));

        CreateCourseLocal createCourseLocal = CreateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local1.getId())
                .build();

        courseLocalService.createCourseLocal(loginCreate(user), createCourseLocal);

        Local local2 = Local.builder()
                .x(String.valueOf(12.4321))
                .y(String.valueOf(121.1212))
                .address(Address.builder()
                        .city("서울시")
                        .street("아리수로 93길 9-14")
                        .zipcode("4층 열람실")
                        .build())
                .areaCategory(AreaCategory.CULTURE)
                .name("도서관")
                .user(user)
                .build();
        localRepository.save(local2);

        AddLocalToCourse addLocalToCourse = AddLocalToCourse.builder()
                .localId(local2.getId())
                .build();

        courseLocalService.addLocalToCourse(loginCreate(user), course.getId(), addLocalToCourse);

        Local local3 = Local.builder()
                .x(String.valueOf(15.1617))
                .y(String.valueOf(128.1281))
                .address(Address.builder()
                        .city("서울시")
                        .street("아리수로 12길 1-14")
                        .zipcode("402")
                        .build())
                .areaCategory(AreaCategory.ETC)
                .name("매장")
                .user(user)
                .build();
        localRepository.save(local3);

        AddLocalToCourse addLocalToCourse2 = AddLocalToCourse.builder()
                .localId(local3.getId())
                .build();

        courseLocalService.addLocalToCourse(loginCreate(user), course.getId(), addLocalToCourse2);

        //when
        UpdateCourseLocal updateCourseLocal = UpdateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local3.getId())
                .turn(0)
                .build();

        courseLocalService.updateCourseLocal(loginCreate(user), updateCourseLocal);

        //then
        List<CourseLocalResponse> courseLocalList = courseLocalService.getCourseLocalList(course.getId());

        assertEquals(1, courseRepository.count());
        assertEquals(3, courseLocalRepository.count());
        assertEquals(0, courseLocalList.get(0).getTurn());
        assertEquals(local3.getId(), courseLocalList.get(0).getLocalId());
        assertEquals(local2.getId(), courseLocalList.get(1).getLocalId());
        assertEquals(local1.getId(), courseLocalList.get(2).getLocalId());
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

    private Local localCreate(User user) {
        return Local.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.CAFE)
                .name("카페 맛집")
                .user(user)
                .build();
    }
}