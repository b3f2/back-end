package com.backend.api.config.factory;

import com.backend.api.entity.post.Category;
import com.backend.api.entity.post.Image;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.User;

import java.util.List;

import static com.backend.api.config.factory.CategoryFactory.createCategory;

public class PostFactory {
    User user;
    public static Post createPost() {
        return createPost(User.builder().build(), createCategory());
    }

    public static Post createPost(User user, Category category) {
        return new Post("title", "content",  user, category, List.of());
    }

    public static Post createPostWithImages(User user, Category category, List<Image> images) {
        return new Post("title", "content", user, category, images);
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", User.builder().build(), createCategory(), images);
    }
}
