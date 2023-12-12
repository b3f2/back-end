package com.backend.api.config.factory;

import com.backend.api.entity.post.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category("name");
    }

    public static Category createCategoryWithName(String name) {
        return new Category(name);
    }
}
