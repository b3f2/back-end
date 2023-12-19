package com.backend.api.event;

import com.backend.api.dto.post.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreateEvent {
    private UserDto publisher;
    private UserDto postWriter;
    private UserDto parentWriter;
    private String Content;
}
