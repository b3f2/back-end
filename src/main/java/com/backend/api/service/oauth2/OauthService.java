package com.backend.api.service.oauth2;

import com.backend.api.entity.user.User;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.oauth2.OauthEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addInformation(LoginResponse response, OauthEdit oauthEdit) {
        User user = userRepository.findByEmail(response.getEmail()).orElseThrow(UserNotFoundException::new);

        String password = passwordEncoder.encode(oauthEdit.getPassword());

        user.addInformation(oauthEdit, password);
    }

}
