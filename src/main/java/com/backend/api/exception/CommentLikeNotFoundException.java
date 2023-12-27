package com.backend.api.exception;

public class CommentLikeNotFoundException extends ApiException {

    private static final String MESSAGE = "댓글 좋아요를 하지 않았습니다.";

    public CommentLikeNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
