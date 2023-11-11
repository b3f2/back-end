package com.backend.api;

import com.backend.api.jwt.TokenProvider;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.service.auth.AuthService;
import com.backend.api.service.user.UserService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTestSupport {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Mock
    protected TokenProvider tokenProvider;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

}
