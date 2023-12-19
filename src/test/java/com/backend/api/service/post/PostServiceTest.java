package com.backend.api.service.post;

import com.backend.api.ServiceTestSupport;
import com.backend.api.config.WithMockCustomUser;
import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.entity.post.Category;
import com.backend.api.entity.post.Image;
import com.backend.api.entity.post.Post;
import com.backend.api.entity.user.Role;
import com.backend.api.entity.user.User;
import com.backend.api.exception.CategoryNotFoundException;
import com.backend.api.exception.PostNotFoundException;
import com.backend.api.exception.UnsupportedImageFormatException;
import com.backend.api.exception.UserNotFoundException;
import com.backend.api.config.factory.PostCreateRequestFactory;
import com.backend.api.request.post.PostCreateRequest;
import com.backend.api.request.post.PostUpdateRequest;
import com.backend.api.response.post.PostDetailResponse;
import com.backend.api.response.post.PostResponse;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.util.Login;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.backend.api.config.factory.PostCreateRequestFactory.createPostCreateRequest;
import static com.backend.api.config.factory.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static com.backend.api.config.factory.PostFactory.createPost;
import static com.backend.api.config.factory.PostFactory.createPostWithImages;
import static com.backend.api.config.factory.PostUpdateRequestFactory.createPostUpdateRequest;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest extends ServiceTestSupport {

    private final static String PROCESS_LIKE_POST = "좋아요 처리 완료";
    private final static String PROCESS_UNLIKE_POST = "좋아요 취소 완료";

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        postLikeRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @Test
    @WithMockCustomUser
    void createTest() throws IOException {
        // given
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();

        List<MultipartFile> images = createImages();

        // when
        postService.create(loginResponse, req, images);

        // then
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getImages()).isNotNull();
    }

    @Test
    void createExceptionByUserNotFoundTest() throws IOException {
        //given
        LoginResponse loginResponse = createLogin();
        List<MultipartFile> images = createImages();

        //when, then
        assertThatThrownBy(()->postService.create(loginResponse, createPostCreateRequest(), images)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @WithMockCustomUser
    void createExceptionByCategoryNotFoundTest() throws IOException {
        //given
        List<MultipartFile> images = createImages();
        LoginResponse loginResponse = createLogin();

        //when, then
        assertThatThrownBy(()->postService.create(loginResponse, PostCreateRequestFactory.createPostCreateRequest(), images)).isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @WithMockCustomUser
    void readTest() throws IOException {
        //given

        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();
        List<MultipartFile> images = createImages();
        postService.create(loginResponse, req, images);

        Post post = postRepository.findAll().get(0);

        //when
        PostDetailResponse postDetail = postService.read(post.getId());

        //then
        assertThat(postDetail.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void readExceptionByPostNotFoundTest() {
        //given

        //when, then
        assertThatThrownBy(()->postService.read(1L)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @WithMockCustomUser
    void readAllTest() throws IOException {
        //given
        Category category = categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();

        List<Post> list = new ArrayList<>();
        List<MultipartFile> images= createImages();

        for (int i = 0; i < 10; i++) {
            PostCreateRequest req = createPostCreateRequest();
            postService.create(loginResponse, req, images);
        }

        //when
        List<PostResponse> postResponses = postService.readAll(new PostReadCondition(1, 1));

        //then
        assertThat(postResponses.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void deleteTest() throws IOException {
        //given
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();
        List<MultipartFile> images = createImages();
        postService.create(loginResponse, req, images);

        Post post = postRepository.findAll().get(0);

        //when
        postService.delete(post.getId(), loginResponse);

        //then
        assertThat(postRepository.findAll()).isNullOrEmpty();
    }

    @Test
    void deleteExceptionNotFoundPostTest() {
        //given
        LoginResponse loginResponse = createLogin();

        //when, then
        assertThatThrownBy(()->postService.delete(1L, loginResponse)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @Transactional
    @WithMockCustomUser
    void updateTest() throws IOException {
        //given
        List<MultipartFile> images = createImages();
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());
        LoginResponse loginResponse = createLogin();
        postService.create(loginResponse, req, images);

        //when
        Post post = postRepository.findAll().get(0);


        ClassPathResource imageC = new ClassPathResource("static/c.png");
        byte[] imageByteC = Files.readAllBytes(imageC.getFile().toPath());

        ClassPathResource imageResource1 = new ClassPathResource("static/test1.png");
        ClassPathResource updateImg = new ClassPathResource("static/c.png");

        byte[] imageByte1 = Files.readAllBytes(imageResource1.getFile().toPath());
        byte[] updateByte = Files.readAllBytes(updateImg.getFile().toPath());

        MockMultipartFile image1 = new MockMultipartFile("test1", "test1.png", MediaType.IMAGE_PNG_VALUE, imageByte1);
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, updateByte);

        List<String> update = List.of(image1.getName(), cFile.getName());

        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("title", "content", update);

        postService.update(post.getId(),loginResponse, postUpdateRequest, List.of(cFile));

        //then
        List<Image> imgs = post.getImages();
        List<String> originNames = images.stream().map(i->i.getName()).collect(toList());
        assertThat(originNames.size()).isEqualTo(2);
    }

    @Test
    void uploadExceptionByPostNotFoundTest() throws IOException {
        //given

        //when, then
        List<MultipartFile> images = createImages();
        LoginResponse loginResponse = createLogin();
        assertThatThrownBy(() -> postService.update(1L,loginResponse, createPostUpdateRequest("title", "content", List.of("a.png", "b.png")), images))
                .isInstanceOf(PostNotFoundException.class);

    }

    @Test
    @WithMockCustomUser
    @DisplayName("게시글 좋아요")
    void likePostTest() throws Exception{
        //given
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();
        List<MultipartFile> images = createImages();
        postService.create(loginResponse, req, images);

        Post post = postRepository.findAll().get(0);

        //when
        String result = postService.likePost(post.getId(), loginResponse);

        //then
        assertThat(result).isEqualTo(PROCESS_LIKE_POST);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("게시글 좋아요 취소")
    void unlikePostTest() throws Exception {
        // given
        PostCreateRequest req = createPostCreateRequest();
        categoryRepository.save(Category.builder()
                .name("cat")
                .build());

        LoginResponse loginResponse = createLogin();
        List<MultipartFile> images = createImages();
        postService.create(loginResponse, req, images);

        Post post = postRepository.findAll().get(0);

        String result = postService.likePost(post.getId(), loginResponse);
        //when
        String result2 = postService.updatePostLike(post.getId(), loginResponse);

        //then
        assertThat(result2).isEqualTo(PROCESS_UNLIKE_POST);
    }

    private LoginResponse createLogin() {
        return LoginResponse.builder()
                .email("userTest@gmail.com")
                .role(Role.ROLE_USER)
                .build();
    }

    private List<MultipartFile> createImages() throws IOException {
        ClassPathResource imageResource1 = new ClassPathResource("static/test1.png");
        ClassPathResource imageResource2 = new ClassPathResource("static/test2.png");

        byte[] imageByte1 = Files.readAllBytes(imageResource1.getFile().toPath());
        byte[] imageByte2 = Files.readAllBytes(imageResource2.getFile().toPath());

        MockMultipartFile image1 = new MockMultipartFile("test1", "test1.png", MediaType.IMAGE_PNG_VALUE,imageByte1);
        MockMultipartFile image2 = new MockMultipartFile("test2", "test2.png", MediaType.IMAGE_PNG_VALUE,imageByte2);

        List<MultipartFile> images = List.of(image1, image2);
        return images;
    }

}
