package com.backend.api.entity.user;

import com.backend.api.entity.util.Address;
import com.backend.api.entity.util.BaseEntity;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    private User(String email, String password, String nickName, Address address, String birth, Gender gender, Role role, String refreshToken) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.address = address;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public static User toEntity(JoinRequest request, String password) {
        return User.builder()
                .email(request.getEmail())
                .password(password)
                .nickName(request.getNickName())
                .address(
        Address.builder()
                .city(request.getCity())
                .street(request.getStreet())
                .zipcode(request.getZipcode())
                .build()
                )
                .birth(request.getBirth())
                .gender(Gender.valueOf(request.getGender()))
                .role(Role.ROLE_USER)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

    public void edit(UserEdit userEdit) {
        this.password = userEdit.getPassword() != null ? userEdit.getPassword() : password;
        this.nickName = userEdit.getNickName() != null ? userEdit.getNickName() : nickName;

        address.edit(userEdit.getCity(), userEdit.getStreet(), userEdit.getZipcode());

    }
}
