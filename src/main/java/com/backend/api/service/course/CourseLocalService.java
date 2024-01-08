package com.backend.api.service.course;

import com.backend.api.entity.course.Course;
import com.backend.api.entity.course.CourseLocal;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.exception.CourseLocalNotFoundException;
import com.backend.api.exception.CourseNotFoundException;
import com.backend.api.exception.LocalNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.course.CourseLocalRepository;
import com.backend.api.repository.course.CourseRepository;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.course.CreateCourseLocal;
import com.backend.api.request.course.UpdateCourseLocal;
import com.backend.api.request.local.AddLocalToCourse;
import com.backend.api.response.course.CourseLocalResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseLocalService {

    private final CourseLocalRepository courseLocalRepository;
    private final CourseRepository courseRepository;
    private final LocalRepository localRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseLocalResponse createCourseLocal(LoginResponse loginResponse, CreateCourseLocal request) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(CourseNotFoundException::new);

        Local local = localRepository.findById(request.getLocalId())
                .orElseThrow(UserNotFoundException::new);

        CourseLocal courseLocal = CourseLocal.builder()
                .course(course)
                .local(local)
                .turn(0)
                .build();

        return CourseLocalResponse.of(courseLocalRepository.save(courseLocal));
    }

    @Transactional
    public CourseLocalResponse addLocalToCourse(LoginResponse loginResponse, Long courseId, AddLocalToCourse request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        Local local = localRepository.findById(request.getLocalId())
                .orElseThrow(LocalNotFoundException::new);

        List<CourseLocal> byCourseId = courseLocalRepository.findByCourseIdOrderByTurn(courseId);

        CourseLocal courseLocal = CourseLocal.builder()
                .course(course)
                .local(local)
                .turn(byCourseId.size())
                .build();

        return CourseLocalResponse.of(courseLocalRepository.save(courseLocal));
    }

    public List<CourseLocalResponse> getCourseLocalList(Long courseId) {
        return courseLocalRepository.findByCourseIdOrderByTurn(courseId)
                .stream()
                .map(CourseLocalResponse::of)
                .toList();
    }

    @Transactional
    public List<CourseLocalResponse> updateCourseLocal(LoginResponse loginResponse, UpdateCourseLocal request) {
        CourseLocal getCourseLocal = courseLocalRepository.findByCourseIdAndLocalId(request.getCourseId(), request.getLocalId())
                .orElseThrow(CourseLocalNotFoundException::new);

        int turn = getCourseLocal.getTurn();

        List<CourseLocal> courseLocals = courseLocalRepository.findByCourseIdOrderByTurn(request.getCourseId());
        Collections.swap(courseLocals, turn, request.getTurn());

        courseLocals.get(request.getTurn()).setTurn(request.getTurn());
        courseLocals.get(turn).setTurn(turn);

        return courseLocals.stream()
                .map(CourseLocalResponse::of)
                .toList();
    }
}
