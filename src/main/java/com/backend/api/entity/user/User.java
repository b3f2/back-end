package com.backend.api.entity.user;

import com.backend.api.entity.util.Address;
import com.backend.api.entity.util.BaseEntity;
import com.backend.api.request.oauth2.OauthEdit;
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

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickName;

    @Embedded
    private Address address;

    private String birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private String oauthId;

    @Builder
    private User(String email, String password, String nickName, Address address, String birth, Gender gender, Role role, String refreshToken, String oauthId) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.address = address;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.refreshToken = refreshToken;
        this.oauthId = oauthId;
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

    public void oauthIdCreate(String oauthId) {
        this.oauthId = oauthId;
    }

    public User addInformation(OauthEdit oauthEdit, String password) {
        return User.builder()
                .password(password)
                .address(Address.builder()
                        .city(oauthEdit.getCity())
                        .street(oauthEdit.getStreet())
                        .zipcode(oauthEdit.getZipcode())
                        .build())
                .birth(oauthEdit.getBirth())
                .gender(Gender.valueOf(oauthEdit.getGender()))
                .build();
    }
}
