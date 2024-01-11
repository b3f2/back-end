package com.backend.api.docs.category;

import com.backend.api.controller.category.CategoryController;
import com.backend.api.docs.RestDocsSupport;
import com.backend.api.entity.post.Category;
import com.backend.api.request.category.CategoryCreateRequest;
import com.backend.api.request.category.CategoryUpdateRequest;
import com.backend.api.response.category.CategoryCreateResponse;
import com.backend.api.response.category.CategoryUpdateResponse;
import com.backend.api.service.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerDocsTest extends RestDocsSupport {
    private final CategoryService categoryService = mock(CategoryService.class);

    @Override
    protected Object initController() {
        return new CategoryController(categoryService);
    }

    @DisplayName("카테고리 생성 API")
    @Test
    void createCategory() throws Exception {
        CategoryCreateRequest req = CategoryCreateRequest.builder()
                .id(1L)
                .name("category1")
                .build();

        given(categoryService.create(any(CategoryCreateRequest.class)))
                .willReturn(
                        CategoryCreateResponse.builder()
                                .id(1L)
                                .build()
                );

        mockMvc.perform(
                post("/api/categories")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("category-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 id")
                        )
                ));
    }

    @DisplayName("카테고리 조회 API")
    @Test
    void readAllCategories() throws Exception {
        given(categoryService.readAll())
                .willReturn(List.of(
                        Category.builder()
                                .id(1L)
                                .name("category1")
                                .build(),
                        Category.builder()
                                .id(2L)
                                .name("category2")
                                .build()
                ));

        mockMvc.perform(
                get("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("category-list",
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
                                        .description("카테고리 Id"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING)
                                        .description("카테고리 이름")
                        )
                ));
    }

    @DisplayName("카테고리 삭제 API")
    @Test
    void deleteCategory() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                delete("/api/categories/{id}", id)
        )
                .andExpect(status().isOk());
    }

    @DisplayName("카테고리 수정 API")
    @Test
    void updateCategory() throws Exception {
        Long id = 1L;
        CategoryUpdateRequest req = CategoryUpdateRequest.builder()
                .name("updated")
                .build();

        given(categoryService.update(anyLong(), any(CategoryUpdateRequest.class)))
                .willReturn(
                        CategoryUpdateResponse.builder()
                                .id(1L)
                                .build()
                );
        mockMvc.perform(
                put("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("category-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("수정 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 id")
                        )
                ));
    }
}
