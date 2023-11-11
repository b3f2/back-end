package com.backend.api.service.user;

import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidSignUpException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.user.JoinRequest;
import com.backend.api.request.user.UserEdit;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(JoinRequest request) {

         if (userRepository.existsByEmail(request.getEmail())) {
             throw new InvalidSignUpException();
         }

        String password = passwordEncoder.encode(request.getPassword());

        User user = userRepository.save(User.toEntity(request, password));

        return UserResponse.of(user);
    }

    public UserResponse get(LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse edit(LoginResponse loginResponse, UserEdit userEdit) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (userEdit.getPassword() != null) {
             userEdit.encode(passwordEncoder.encode(userEdit.getPassword()));
        }

        user.edit(userEdit);

        return UserResponse.of(user);
    }

    @Transactional
    public void delete(LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}
