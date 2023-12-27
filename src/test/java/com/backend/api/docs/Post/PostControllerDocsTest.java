package com.backend.api.docs.Post;

import com.backend.api.controller.post.PostController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.entity.post.Category;
import com.backend.api.response.post.PostDetailResponse;
import com.backend.api.response.post.PostResponse;
import com.backend.api.service.post.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerDocsTest extends RestDocsSupport {

    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostController(postService);
    }

    @DisplayName("게시글 목록 조회 API")
    @Test
    void readAll() throws Exception {
        List<PostResponse> responses = LongStream.range(0, 9).mapToObj(i->PostResponse.builder()
                .id(i)
                .title("title"+i)
                .userid(i)
                .content("hello")
                .category(Category.builder().id(1L).name("cat").build())
                .build()
        ).collect(Collectors.toList());
        given(postService.readAll(any(PostReadCondition.class)))
                .willReturn(
                responses
            );
        mockMvc.perform(
                get("/api/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                //.andExpect(status().isOk())
                .andDo(document("post-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("게시글 id"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data[].userid").type(JsonFieldType.NUMBER)
                                        .description("게시글 작성자id"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING)
                                        .description("게시글 내용"),
                                fieldWithPath("data[].category.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 분류 ID"),
                                fieldWithPath("data[].category.name").type(JsonFieldType.STRING)
                                        .description("게시글 분류 이름")
                        )
                ));
    }

    @DisplayName("게시글 단건 조회 API")
    @Test
    void read() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();

        List<String> images = List.of("test1.png", "test2.png");
        PostDetailResponse response = PostDetailResponse.builder()
                .postId(1L)
                .userId(1L)
                .nickname("nick")
                .content("content")
                .title("title")
                .imgList(images)
                .commentList(List.of())
                .categoryId(1L)
                .categoryname("cat")
                .createTime(localDateTime)
                .build();
        given(postService.read(anyLong()))
                .willReturn(
                        response
                );
        mockMvc.perform(
                get("/api/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.postId").type(JsonFieldType.NUMBER)
                                        .description("게시글 Id"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("회원id"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("회원 닉네임"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.imgList").type(JsonFieldType.ARRAY)
                                        .description("게시글 이미지 목록"),
                                fieldWithPath("data.commentList").type(JsonFieldType.ARRAY)
                                        .description("게시글 댓글 목록"),
                                fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER)
                                        .description("게시글 카테고리"),
                                fieldWithPath("data.categoryName").type(JsonFieldType.STRING)
                                        .description("게시글 카테고리 이름"),
                                fieldWithPath("data.createTime").type(JsonFieldType.ARRAY)
                                        .description("게시글 생성일")
                        )
                ));
    }

//    @DisplayName("게시글 작성 API")
//    @Test
//    void createPost() throws Exception {
//        MultipartFile test1 = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
//        MultipartFile test2 = new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes());
//
//        List<MultipartFile> images = List.of(test1, test2);
//        PostCreateRequest request = new PostCreateRequest("title", "content", 1L, 1L, images);
//
//        given(postService.create(any(LoginResponse.class), eq(request), eq(images)))
//                .willReturn(
//                        PostCreateResponse.builder()
//                                .id(1L)
//                                .build()
//                );
//
//        mockMvc.perform(
//                multipart("/api/posts")
//                        .file("images", request.getImages().get(0).getBytes())
//                        .file("images", request.getImages().get(1).getBytes())
//                        .param("title", request.getTitle())
//                        .param("content", request.getContent())
//                        .param("categoryId", String.valueOf(request.getCategoryId()))
//                        .param("userId", String.valueOf(request.getUserId()))
//                        .with(requestPostProcessor -> {
//                            requestPostProcessor.setMethod("POST");
//                            return requestPostProcessor;
//                        })
//                        .contentType(MULTIPART_FORM_DATA)
//                        .characterEncoding("UTF-8")
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-create",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
////                        requestFields(
////                                fieldWithPath("images").type(JsonFieldType.ARRAY)
////                                        .description("이미지 파일"),
////                                fieldWithPath("title").type(JsonFieldType.STRING)
////                                        .description("게시글 제목"),
////                                fieldWithPath("content").type(JsonFieldType.STRING)
////                                        .description("게시글 내용"),
////                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER)
////                                        .description("카테고리 Id"),
////                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
////                                        .description("사용자 Id")
////                        ),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER)
//                                        .description("코드"),
//                                fieldWithPath("status").type(JsonFieldType.STRING)
//                                        .description("상태"),
//                                fieldWithPath("message").type(JsonFieldType.STRING)
//                                        .description("메세지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT)
//                                        .description("응답 데이터"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
//                                        .description("게시글 id")
//                        )
//                ));
//    }

//    @DisplayName("게시글 수정 API")
//    @Test
//    void updatePost() throws Exception {
//        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", "image/png", "image1".getBytes());
//        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", "image/png", "image2".getBytes());
//
//        List<MultipartFile> images = List.of(image1, image2);
//
//        PostUpdateRequest request = PostUpdateRequest.builder()
//                .title("title")
//                .content("content")
//                .build();
//        given(postService.update(anyLong(), LoginResponse.builder().build(), any(PostUpdateRequest.class), images))
//                .willReturn(PostUpdateResponse.builder()
//                        .id(1L)
//                        .build()
//                );
//        mockMvc.perform(
//                multipart("/api/posts/{id}", 1L)
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(APPLICATION_JSON)
//                )
//                        .andDo(print())
//                        .andExpect(status().isOk())
//                        .andDo(document("post-update",
//                                preprocessRequest(prettyPrint()),
//                                preprocessResponse(prettyPrint()),
//                                requestFields(
//                                        fieldWithPath("title").type(JsonFieldType.STRING)
//                                                .description("제목"),
//                                        fieldWithPath("content").type(JsonFieldType.STRING)
//                                                .description("내용"),
//                                        fieldWithPath("addedImages").type(JsonFieldType.ARRAY)
//                                                .description("추가된 사진들"),
//                                        fieldWithPath("deletedImages").type(JsonFieldType.ARRAY)
//                                                .description("제거된 사진들")
//                                ),
//                                responseFields(
//                                        fieldWithPath("code").type(JsonFieldType.NUMBER)
//                                                .description("코드"),
//                                        fieldWithPath("status").type(JsonFieldType.STRING)
//                                                .description("상태"),
//                                        fieldWithPath("message").type(JsonFieldType.STRING)
//                                                .description("메세지"),
//                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
//                                                .description("응답 데이터"),
//                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
//                                                .description("게시글id")
//                                )
//                        ));
//    }

    @DisplayName("게시글 삭제 API")
    @Test
    void deletePost() throws Exception {
        mockMvc.perform(
                        delete("/api/posts/{id}", 1L)
                                .contentType(APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
    }

    @DisplayName("게시글 좋아요 및 취소 API")
    @Test
    void likePost() throws Exception {
        mockMvc.perform(
                post("/api/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("likeOrNot-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
    }
}
