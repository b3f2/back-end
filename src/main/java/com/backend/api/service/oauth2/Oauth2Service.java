package com.backend.api.service.oauth2;

import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.oauth2.Oauth2Request;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Oauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var delegate = new DefaultOAuth2UserService();
        var oAuth2User = delegate.loadUser(userRequest);

        // Oauth 에서 담고 있는 ID
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Oauth 플랫폼
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // Oauth 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Oauth2Request request = OAuthAttributes.extract(registrationId, attributes);

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        // Oauth로 로그인 시 회원 정보가 없으면 새로 Oauth 정보 바탕으로 유저를 생성한다.
        if (user.isEmpty()) {
            return OauthUserCreate(request, attributes, userNameAttributeName);
        } else {
            // Oauth로 로그인 시 유저의 정보가 있는데 Oauth로 로그인 기록이 없으면 OauthID를 주입시켜준다.
            oauthIdCreate(user.get(), request);

            return new DefaultOAuth2User(
                    Collections.singleton(
                            new SimpleGrantedAuthority(user.get().getRole().getKey())),
                    attributes,
                    userNameAttributeName
            );
        }
    }

    @Transactional
    public OAuth2User OauthUserCreate(Oauth2Request request, Map<String, Object> attributes, String userNameAttributeName) {

        String password = UUID.randomUUID().toString();
//        String encodedPassword = passwordEncoder.encode(password);
        User user = userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(password)
                        .nickName(request.getName())
                        .oauthId((request.getOauthId()))
                        .role(Role.ROLE_USER)
                        .build()
        );

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRole().getKey())),
                        attributes,
                        userNameAttributeName
                        );
    }

    @Transactional
    public void oauthIdCreate(User user,Oauth2Request request) {
        user.oauthIdCreate(request.getOauthId());
    }
}
