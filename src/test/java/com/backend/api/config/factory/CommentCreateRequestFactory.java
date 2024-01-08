package com.backend.api.config.factory;

import com.backend.api.request.comment.CommentCreateRequest;

public class CommentCreateRequestFactory {
    public static CommentCreateRequest createCommentCreateRequest() {
        return new CommentCreateRequest("content", 1L, 1L, null);
    }

    public static CommentCreateRequest createCommentCreateRequestWithParentId(Long parentId) {
        return new CommentCreateRequest("content", 1L, 1L, parentId);
    }
    public static CommentCreateRequest createCommentCreateRequestWithParentId(Long postId, Long parentId) {
        return new CommentCreateRequest("content", postId, 1L, parentId);
    }
}

