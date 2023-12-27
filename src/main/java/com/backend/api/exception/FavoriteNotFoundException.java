package com.backend.api.exception;

public class FavoriteNotFoundException extends ApiException{

    private static final String MESSAGE = "해당 즐겨찾기 폴더가 없습니다.";
    public FavoriteNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
