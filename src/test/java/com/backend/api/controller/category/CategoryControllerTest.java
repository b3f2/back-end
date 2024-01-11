package com.backend.api.controller.category;

import com.backend.api.ControllerTestSupport;
import com.backend.api.request.category.CategoryCreateRequest;
import com.backend.api.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest extends ControllerTestSupport {
    @InjectMocks
    CategoryController categoryController;

    @Mock
    CategoryService categoryService;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    @DisplayName("카테고리 생성")
    void createTest() throws Exception {
        //given
        CategoryCreateRequest req = new CategoryCreateRequest(1L, "category1");

        //when, then
        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateTest() throws Exception {
        //given
        String name = "updated";

        //when, then
        mockMvc.perform(
                put("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(name))
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 조회")
    void findAllTest() throws Exception {
        //given, when, then
        mockMvc.perform(
                get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deletetest() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id)
        )
                .andExpect(status().isOk());
    }

}
