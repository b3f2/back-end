package com.backend.api.service.local;

import com.backend.api.entity.local.Local;
import com.backend.api.entity.local.LocalReview;
import com.backend.api.entity.user.User;
import com.backend.api.exception.InvalidUserException;
import com.backend.api.exception.LocalNotFoundException;
import com.backend.api.exception.LocalReviewNotFoundException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.repository.local.LocalRepository;
import com.backend.api.repository.local.LocalReviewRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.local.CreateLocalReview;
import com.backend.api.request.local.UpdateLocalReview;
import com.backend.api.response.local.LocalReviewResponse;
import com.backend.api.response.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalReviewService {

    private final LocalReviewRepository localReviewRepository;
    private final LocalRepository localRepository;
    private final UserRepository userRepository;

    @Transactional
    public LocalReviewResponse createLocalReview(LoginResponse loginResponse, CreateLocalReview createLocalReview) {
        User user = userRepository.findByEmail(loginResponse.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Local local = localRepository.findById(createLocalReview.getLocalId())
                .orElseThrow(LocalNotFoundException::new);

        LocalReview localReview = LocalReview.builder()
                .content(createLocalReview.getContent())
                .rating(createLocalReview.getRating())
                .local(local)
                .user(user)
                .build();

        return LocalReviewResponse.of(localReviewRepository.save(localReview));
    }


    public LocalReviewResponse getLocalReview(Long id) {
        LocalReview localReview = localReviewRepository.findById(id)
                .orElseThrow(LocalReviewNotFoundException::new);

        return LocalReviewResponse.builder()
                .content(localReview.getContent())
                .rating(localReview.getRating())
                .build();
    }

    @Transactional
    public LocalReviewResponse updateLocalReview(LoginResponse loginResponse, Long id, UpdateLocalReview updateLocalReview) {
        Local local = localRepository.findById(updateLocalReview.getLocalId())
                .orElseThrow(LocalNotFoundException::new);

        LocalReview localReview = localReviewRepository.findById(id)
                .orElseThrow(LocalReviewNotFoundException::new);

        if (!localReview.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        localReview.update(updateLocalReview.getContent(), updateLocalReview.getRating());

        return LocalReviewResponse.of(localReview);
    }

    @Transactional
    public void deleteLocalReview(LoginResponse loginResponse, Long id) {
        LocalReview localReview = localReviewRepository.findById(id)
                .orElseThrow(LocalReviewNotFoundException::new);

        if (!localReview.getUser().getEmail().equals(loginResponse.getEmail())) {
            throw new InvalidUserException();
        }

        localReviewRepository.delete(localReview);
    }

    public List<LocalReviewResponse> getLocalReviewByUserId(Long userId) {
        return localReviewRepository.findByUserId(userId)
                .stream()
                .map(LocalReviewResponse::new)
                .toList();
    }
}
