package com.backend.api.service.local;

import com.backend.api.entity.local.Local;
import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidUserException;
import com.backend.api.exception.LocalNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.local.CreateLocal;
import com.backend.api.response.local.LocalResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalService {

    private final UserRepository userRepository;
    private final LocalRepository localRepository;

    @Transactional
    public LocalResponse createLocal(CreateLocal request, LoginResponse loginResponse) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Local local = Local.builder()
                .name(request.getName())
                .x(request.getX())
                .y(request.getY())
                .address(request.getAddress())
                .areaCategory(request.getAreaCategory())
                .user(user)
                .build();

        return LocalResponse.of(localRepository.save(local));
    }

    public LocalResponse getLocal(Long id) {
        Local local = localRepository.findById(id)
                .orElseThrow(LocalNotFoundException::new);

        return LocalResponse.of(local);
    }

    public List<LocalResponse> getLocalList() {
        return localRepository.findAll().stream()
                .map(LocalResponse::of)
                .toList();
    }

    public List<LocalResponse> getLocalsByUser(Long userId) {
        return localRepository.findByUserId(userId).stream()
                .map(LocalResponse::of)
                .toList();
    }

    @Transactional
    public void deleteLocal(LoginResponse loginResponse, Long id) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(!user.getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        localRepository.findById(id)
                .orElseThrow(LocalNotFoundException::new);

        localRepository.deleteById(id);
    }
}
