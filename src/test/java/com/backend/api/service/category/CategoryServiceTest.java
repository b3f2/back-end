package com.backend.api.service.category;

import com.backend.api.ServiceTestSupport;
import com.backend.api.entity.post.Category;
import com.backend.api.exception.InvalidCategoryIdException;
import com.backend.api.exception.InvalidCategoryNameException;
import com.backend.api.request.category.CategoryCreateRequest;
import com.backend.api.request.category.CategoryUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CategoryServiceTest extends ServiceTestSupport {
    @AfterEach
    void teamDown() {
        categoryRepository.deleteAllInBatch();
    }

    @Test
    void createTest() {
        //given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "category1");

        //when
        categoryService.create(request);

        //then
        assertThat(categoryRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void createWithAlreadyExistNameTest() {
        //given
        CategoryCreateRequest request1 = new CategoryCreateRequest(1L, "category1");
        categoryService.create(request1);

        //when
        CategoryCreateRequest request2 = new CategoryCreateRequest(2L, "category1");

        //then
        assertThatThrownBy(()->categoryService.create(request2))
                .isInstanceOf(InvalidCategoryNameException.class)
                .hasMessage("이미 존재하는 카테고리 이름입니다.");
    }

    @Test
    void createWithAlreadyExistIdTest() {
        //given
        CategoryCreateRequest request1 = new CategoryCreateRequest(1L, "category1");
        categoryService.create(request1);

        //when
        CategoryCreateRequest request2 = new CategoryCreateRequest(1L, "category2");

        //then
        assertThatThrownBy(()->categoryService.create(request2))
                .isInstanceOf(InvalidCategoryIdException.class)
                .hasMessage("이미 존재하는 카테고리 Id입니다.");
    }

    @Test
    void readAllTest() {
        //given
        CategoryCreateRequest request1 = new CategoryCreateRequest(1L, "category1");
        CategoryCreateRequest request2 = new CategoryCreateRequest(2L, "category2");
        categoryService.create(request1);
        categoryService.create(request2);

        //when
        List<Category> result = categoryService.readAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void deleteTest() {
        //given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "cateogory1");
        categoryService.create(request);
        Category category = categoryRepository.findAll().get(0);

        //when
        categoryService.delete(category.getId());

        //then
        assertThat(categoryRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void updateTest() {
        //given
        CategoryCreateRequest request = new CategoryCreateRequest(1L, "category1");
        categoryService.create(request);
        Category category = categoryRepository.findAll().get(0);

        //when
        CategoryUpdateRequest req = new CategoryUpdateRequest("updated");
        categoryService.update(category.getId(), req);

        //then
        assertThat(categoryRepository.findAll().size()).isEqualTo(1);
        assertThat(categoryRepository.findAll().get(0).getName()).isEqualTo("updated");
    }
}
