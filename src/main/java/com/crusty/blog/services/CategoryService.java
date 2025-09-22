package com.crusty.blog.services;

import com.crusty.blog.domain.entities.Category;
import com.crusty.blog.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


public interface CategoryService {

    List<Category> allCategories();
    Category createCategory(Category category);
    void deleteCategory(UUID uuid);
    Category getCategory(UUID uuid);
}
