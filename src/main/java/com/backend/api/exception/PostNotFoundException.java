package com.backend.api.exception;

public class PostNotFoundException extends ApiException {

    private static final String MESSAGE = "등록된 게시물을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
