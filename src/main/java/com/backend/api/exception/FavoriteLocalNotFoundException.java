package com.backend.api.exception;

public class FavoriteLocalNotFoundException extends ApiException{

    private static final String MESSAGE = "Favorites id와 Local id에 해당하는 FavoriteLocal이 존재하지 않습니다.";
    public FavoriteLocalNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
