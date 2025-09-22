package com.crusty.blog.services.impl;

import com.crusty.blog.domain.entities.Category;
import com.crusty.blog.repositories.CategoryRepo;
import com.crusty.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public List<Category> allCategories() {
        return categoryRepo.findAllWithArticleCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepo.existsByNameIgnoreCase(category.getName()))
        {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepo.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID uuid) {
        Category category = getCatById(uuid);
        if(!category.getAllArticles().isEmpty())
        {
            throw new IllegalStateException("Cannot delete category "+ category.getName()+" as it has posts left : "+ category.getAllArticles().size());

        }
        categoryRepo.deleteById(uuid);
    }

    @Override
    public Category getCategory(UUID uuid) {
        return categoryRepo.findById(uuid).orElseThrow(()-> new EntityNotFoundException("Couldnt find category with id " + uuid));
    }

    //helper
    Category getCatById(UUID uuid)
    {
        return categoryRepo.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No category exists with id "+ uuid));
    }
}
