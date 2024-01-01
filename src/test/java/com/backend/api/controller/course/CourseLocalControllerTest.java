package com.backend.api.controller.course;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseLocal;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.course.CreateCourseLocal;
import com.backend.api.request.course.UpdateCourseLocal;
import com.backend.api.request.local.AddLocalToCourse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class CourseLocalControllerTest extends ControllerTestSupport {

    @AfterEach
    void setUp() {
        courseLocalRepository.deleteAllInBatch();
        courseRepository.deleteAllInBatch();
        localRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("CourseLocal 생성")
    @WithMockCustomUser
    void createCourseLocal() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Course course = courseCreate(user);
        courseRepository.save(course);

        Local local = localCreate(user);
        localRepository.save(local);

        CreateCourseLocal courseLocal = CreateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local.getId())
                .build();

        //expect
        mockMvc.perform(post("/api/course-local")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseLocal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.turn").value(0))
                .andExpect(jsonPath("$.data.courseId").value(course.getId()))
                .andExpect(jsonPath("$.data.localId").value(local.getId()));

        assertEquals(1, courseLocalRepository.count());

    }

    @Test
    @DisplayName("코스 안에 장소 넣기")
    @WithMockCustomUser
    void addLocalToCourse() throws Exception {
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Course course = courseCreate(user);
        courseRepository.save(course);

        Local local = localCreate(user);
        localRepository.save(local);

        CourseLocal courseLocal = CourseLocal.builder()
                .course(course)
                .local(local)
                .turn(0)
                .build();
        courseLocalRepository.save(courseLocal);

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
        //expect
        mockMvc.perform(post("/api/course-local/{courseId}", course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addLocalToCourse)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.turn").value(1))
                .andExpect(jsonPath("$.data.courseId").value(course.getId()))
                .andExpect(jsonPath("$.data.localId").value(local2.getId()));

        assertEquals(2, courseLocalRepository.count());
    }

    @Test
    @DisplayName("코스 안에 담긴 장소 조회하기")
    @WithMockCustomUser
    void getCourseLocalList() throws Exception{
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Course course = courseCreate(user);
        courseRepository.save(course);

        Local local = localCreate(user);
        localRepository.save(local);

        CourseLocal courseLocal = CourseLocal.builder()
                .course(course)
                .local(local)
                .turn(0)
                .build();
        courseLocalRepository.save(courseLocal);

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
        //expect
        mockMvc.perform(post("/api/course-local/{courseId}", course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addLocalToCourse)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.turn").value(1))
                .andExpect(jsonPath("$.data.courseId").value(course.getId()))
                .andExpect(jsonPath("$.data.localId").value(local2.getId()));

        assertEquals(2, courseLocalRepository.count());
    }

    @Test
    @DisplayName("코스 안에 장소 순서 바꾸기")
    @WithMockCustomUser
    void updateCourseLocal() throws Exception{
        //given
        User user = userRepository.findByEmail("user@gmail.com").get();

        Course course = courseCreate(user);
        courseRepository.save(course);

        Local local1 = localCreate(user);
        localRepository.save(local1);

        CourseLocal courseLocal = CourseLocal.builder()
                .course(course)
                .local(local1)
                .turn(0)
                .build();
        courseLocalRepository.save(courseLocal);

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

        CourseLocal courseLocal2 = CourseLocal.builder()
                .course(course)
                .local(local2)
                .turn(1)
                .build();
        courseLocalRepository.save(courseLocal2);

        Local local3 = Local.builder()
                .x(String.valueOf(21.2121))
                .y(String.valueOf(132.1321))
                .address(Address.builder()
                        .city("서울시")
                        .street("천호대로 12길 443")
                        .zipcode("212")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("쭈꾸미 집")
                .user(user)
                .build();
        localRepository.save(local3);

        CourseLocal courseLocal3 = CourseLocal.builder()
                .course(course)
                .local(local3)
                .turn(2)
                .build();
        courseLocalRepository.save(courseLocal3);

        //장소 3개 id 1= [0], id 2= [1], id 3= [2] 순서로 저장하고
        //장소 2번을 1번으로 옮기면 id 1= [1], id 2= [0], id 3= [2] 순서로 저장되어야 한다.
        UpdateCourseLocal updateCourseLocal = UpdateCourseLocal.builder()
                .courseId(course.getId())
                .localId(local2.getId())
                .turn(0)
                .build();

        //expect
        mockMvc.perform(patch("/api/course-local")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseLocal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].localId").value(local2.getId()));

        assertEquals(3, courseLocalRepository.count());

        List<CourseLocal> byCourseIdOrderByTurn = courseLocalRepository.findByCourseIdOrderByTurn(course.getId());
        assertEquals(0, byCourseIdOrderByTurn.get(0).getTurn());
        assertEquals(1, byCourseIdOrderByTurn.get(1).getTurn());
        assertEquals(2, byCourseIdOrderByTurn.get(2).getTurn());
        assertEquals(local2.getId(), byCourseIdOrderByTurn.get(0).getLocal().getId());
        assertEquals(local1.getId(), byCourseIdOrderByTurn.get(1).getLocal().getId());
        assertEquals(local3.getId(), byCourseIdOrderByTurn.get(2).getLocal().getId());
    }

    @Test
    @DisplayName("없는 CourseLocal 수정하기")
    @WithMockCustomUser
    void updateEmptyCourseLocal() throws Exception{
        //given
        UpdateCourseLocal updateCourseLocal = UpdateCourseLocal.builder()
                .courseId(1L)
                .localId(1L)
                .build();
        //expect
        mockMvc.perform(patch("/api/course-local")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseLocal)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("CourseId 혹은 LocalId에 해당하는 CourseLocal이 존재하지 않습니다."));
    }

    private Course courseCreate(User user) {
        return Course.builder()
                .name("코스")
                .user(user)
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