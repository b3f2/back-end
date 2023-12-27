package com.backend.api.config.factory;

import com.backend.api.request.post.PostUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostUpdateRequestFactory {
    public static PostUpdateRequest createPostUpdateRequest(String title, String content, List<String> imgUrl) {
        return new PostUpdateRequest(title, content, imgUrl);
    }
}
