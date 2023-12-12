package com.backend.api.repository.post;

import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.dto.post.PostSimpleDto;
import com.backend.api.entity.post.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findAllByCondition(PostReadCondition cond);
}
