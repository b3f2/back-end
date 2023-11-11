package com.backend.api.service.auth;

import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidLoginException;
import com.backend.api.exception.InvalidTokenException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.jwt.TokenProvider;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.LoginRequest;
import com.backend.api.request.util.RefreshToken;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        TokenResponse token = tokenProvider.createToken(user.getEmail());

        user.updateRefreshToken(token.getRefreshToken().getData());

        return token;

    }

    @Transactional
    public TokenResponse reissueToken(RefreshToken refreshToken) {
        String refreshTokenValue = refreshToken.getData();

        if (!tokenProvider.isTokenValid(refreshTokenValue)) {
            throw new InvalidTokenException();
        }

        User user = userRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(UserNotFoundException::new);

        TokenResponse token = tokenProvider.createToken(user.getEmail());

        user.updateRefreshToken(token.getRefreshToken().getData());

        return token;
    }

    @Transactional
    public void logout(LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        user.invalidateRefreshToken();

    }
}
