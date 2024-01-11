package com.backend.api.service.category;

import com.backend.api.entity.post.Category;
import com.backend.api.exception.CategoryNotFoundException;
import com.backend.api.exception.InvalidCategoryIdException;
import com.backend.api.exception.InvalidCategoryNameException;
import com.backend.api.repository.category.CategoryRepository;
import com.backend.api.request.category.CategoryCreateRequest;
import com.backend.api.request.category.CategoryUpdateRequest;
import com.backend.api.response.category.CategoryCreateResponse;
import com.backend.api.response.category.CategoryUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryCreateResponse create(CategoryCreateRequest request) {
        if(categoryRepository.existsById(request.getId())) {
            throw new InvalidCategoryIdException();
        } else if(categoryRepository.existsByName(request.getName())) {
            throw new InvalidCategoryNameException();
        }

        Category category = categoryRepository.save(new Category(request.getId(), request.getName()));
        return new CategoryCreateResponse(category.getId());
    }

    public List<Category> readAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryUpdateResponse update(Long id, CategoryUpdateRequest req) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.setName(req.getName());
        Category updated = categoryRepository.save(category);
        return new CategoryUpdateResponse(updated.getId());
    }

}
