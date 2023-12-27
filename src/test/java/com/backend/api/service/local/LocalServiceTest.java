package com.backend.api.service.local;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.request.local.CreateLocal;
import com.backend.api.response.local.LocalResponse;
import com.backend.api.response.user.LoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalServiceTest extends ServiceTestSupport {

    @AfterEach
    void setUp() {
        localRepository.deleteAllInBatch();
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

    @Test
    @DisplayName("사용자와 장소 Dto를 이용해서 장소를 생성한다.")
    @Transactional
    void createLocal() {
        //given
        User user= userCreate();
        userRepository.save(user);

        CreateLocal local = CreateLocal.builder()
                .x(String.valueOf(37.5665))
                .y(String.valueOf(126.9780))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 12길 34")
                        .zipcode("405동 607호")
                        .build())
                .areaCategory(AreaCategory.CAFE)
                .name("카페 맛집")
                .build();

        //when
        localService.createLocal(local, loginCreate(user));

        //then
        assertEquals(1, localRepository.count());
        assertEquals("카페 맛집", localRepository.findAll().get(0).getName());
        assertEquals("user@gmail.com", localRepository.findAll().get(0).getUser().getEmail());
    }

    @Test
    @DisplayName("장소를 만들고 단건 조회한다.")
    void getLocal() {
        //given
        User user= userCreate();
        userRepository.save(user);

        Local local = localCreate(user);
        localRepository.save(local);

        //when
        LocalResponse response = localService.getLocal(local.getId());

        //then
        assertEquals(response.getName(),"카페 맛집");
        assertEquals(response.getX(),"37.5665");
    }

    @Test
    @DisplayName("장소 여러개를 만들고 전체 조회한다.")
    void getLocalList() {
        //given
        User user = userCreate();
        userRepository.save(user);

        Local local = Local.builder()
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

        Local local2 = Local.builder()
                .x(String.valueOf(12.3456))
                .y(String.valueOf(789.1230))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 55길 66")
                        .zipcode("666동 777호")
                        .build())
                .areaCategory(AreaCategory.RESTAURANT)
                .name("음식 맛집")
                .user(user)
                .build();

        Local local3 = Local.builder()
                .x(String.valueOf(15.1515))
                .y(String.valueOf(35.3535))
                .address(Address.builder()
                        .city("서울시")
                        .street("상암로 1길 3")
                        .zipcode("101동 102호")
                        .build())
                .areaCategory(AreaCategory.CULTURE)
                .name("괜찮은 영화관")
                .user(user)
                .build();

        localRepository.saveAll(List.of(local, local2, local3));

        //when
        List<LocalResponse> response = localService.getLocalList();

        //then
        assertEquals(response.size(), 3);
        assertEquals(response.get(0).getName(), "카페 맛집");
        assertEquals(response.get(1).getAreaCategory(), AreaCategory.RESTAURANT);
        assertEquals(response.get(2).getName(), "괜찮은 영화관");
    }

    @Test
    @DisplayName("장소를 만들고 삭제한다.")
    void deleteLocal() {
        //given
        User user= userCreate();
        userRepository.save(user);

        Local local = Local.builder()
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

        localRepository.save(local);

        //when
        localService.deleteLocal(loginCreate(user), local.getId());

        //then
        assertEquals(0, localRepository.count());

    }
}