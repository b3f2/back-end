package com.backend.api.controller.post;

import com.backend.api.ControllerTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.entity.post.Category;
import com.backend.api.entity.post.Post;
import com.backend.api.exception.PostNotFoundException;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.request.post.PostCreateRequest;
import com.backend.api.request.post.PostUpdateRequest;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.backend.api.config.factory.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest extends ControllerTestSupport {
    @InjectMocks
    PostController postController;
    @Mock
    PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    @DisplayName("게시글 작성")
    void createTest() throws Exception {
        //given
        List<MultipartFile> images = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        PostCreateRequest req = new PostCreateRequest("title", "content", 1L, 1L, images);
        // when, then
        mockMvc.perform(
                        multipart("/api/posts")
                                .file("images", images.get(0).getBytes())
                                .file("images", images.get(1).getBytes())
                                .param("title", req.getTitle())
                                .param("content", req.getContent())
                                .param("categoryId", String.valueOf(req.getCategoryId()))
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 단건 조회")
    void readTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                get("/api/posts/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 삭제")
    void deleteTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                delete("/api/posts/{id}", id)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 수정")
    void updateTest() throws Exception {
        //given
        List<MultipartFile> addedImages = List.of(
                new MockMultipartFile("test1", "test1.png", MediaType.IMAGE_PNG_VALUE,"test1".getBytes()),
                new MockMultipartFile("test2", "test2.png", MediaType.IMAGE_PNG_VALUE,"test2".getBytes())
        );
        List<Long> deletedImages = List.of(1L, 2L);
        PostUpdateRequest req = new PostUpdateRequest("title", "content", List.of("image1.png", "image2.png"));

        //when, then
        mockMvc.perform(
                multipart("/api/posts/{id}",1L)
                        .file("addedImages", addedImages.get(0).getBytes())
                        .file("addedImages", addedImages.get(1).getBytes())
                        .param("deletedImages", String.valueOf(deletedImages.get(0)), String.valueOf(deletedImages.get(1)))
                        .param("title", req.getTitle())
                        .param("content", req.getContent())
                        .with(requestPostProcessor ->{
                            requestPostProcessor.setMethod("PUT");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 게시물 조회(페이징)")
    void readAllTest() throws Exception {
        //given
        PostReadCondition cond = new PostReadCondition(0,1);

        //when, then
        mockMvc.perform(
                get("/api/posts")
                        .param("page", String.valueOf(cond.getPage()))
                        .param("size", String.valueOf(cond.getSize()))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요 및 취소")
    void likeOrUnlikePostTest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                post("/api/posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }
}
