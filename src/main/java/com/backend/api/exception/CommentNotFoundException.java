package com.backend.api.exception;

public class CommentNotFoundException extends ApiException {
    private static final String MESSAGE = "등록된 댓글을 찾을 수 없습니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
