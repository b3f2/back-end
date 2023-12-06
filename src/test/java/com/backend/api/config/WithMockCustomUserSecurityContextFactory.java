package com.backend.api.config;

import com.backend.api.entity.user.Gender;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.entity.util.Address;
import com.backend.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        User user = User.builder()
                .email(annotation.email())
                .password(annotation.password())
                .nickName(annotation.nickName())
                .address(Address.builder()
                        .city("서울시")
                        .street("도봉로 106길 23")
                        .zipcode("102동 208호")
                        .build())
                .birth("20000418")
                .gender(Gender.MAN)
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");

        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        annotation.email(),
                        null,
                        List.of(role)
                        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
