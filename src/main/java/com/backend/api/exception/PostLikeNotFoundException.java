package com.backend.api.exception;

public class PostLikeNotFoundException extends ApiException {

    private static final String MESSAGE = "게시물 좋아요를 하지 않았습니다.";

    public PostLikeNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
