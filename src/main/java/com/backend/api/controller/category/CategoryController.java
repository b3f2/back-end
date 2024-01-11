package com.backend.api.controller.category;

import com.backend.api.controller.ApiResponse;
import com.backend.api.entity.post.Category;
import com.backend.api.request.category.CategoryCreateRequest;
import com.backend.api.request.category.CategoryUpdateRequest;
import com.backend.api.response.category.CategoryCreateResponse;
import com.backend.api.response.category.CategoryResponse;
import com.backend.api.response.category.CategoryUpdateResponse;
import com.backend.api.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    @DisplayName("카테고리 생성")
    public ApiResponse<CategoryCreateResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        return ApiResponse.ok(categoryService.create(request));
    }

    @GetMapping("/categories")
    @DisplayName("카테고리 조회")
    public ApiResponse<List<Category>> readAll() {
        return ApiResponse.ok(categoryService.readAll());
    }

    @DeleteMapping("/categories/{id}")
    @DisplayName("카테고리 삭제")
    public ApiResponse<CategoryResponse> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK)
                .message("카테고리 삭제 성공")
                .build();
    }

    @PutMapping("/categories/{id}")
    @DisplayName("카테고리 수정")
    public ApiResponse<CategoryUpdateResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest req) {
        return ApiResponse.of(HttpStatus.OK, categoryService.update(id, req));
    }
}
