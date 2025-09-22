package com.crusty.blog.controllers;


import com.crusty.blog.domain.dtos.CategoryDto;
import com.crusty.blog.domain.dtos.CreateCategoryRequest;
import com.crusty.blog.domain.entities.Category;
import com.crusty.blog.mappers.CategoryMapper;
import com.crusty.blog.repositories.CategoryRepo;
import com.crusty.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> allcats = categoryService.allCategories();
        List<CategoryDto> allDtos = allcats.stream().map(
                categoryMapper::toDto
        ).toList();
        return ResponseEntity.ok(allDtos);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest)
    {
            Category category = categoryMapper.fromRequestToEntity(categoryRequest);

            return new ResponseEntity<>(
                    categoryMapper.toDto(categoryService.createCategory(category)),
                    HttpStatus.CREATED
            );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID uuid)
    {
        categoryService.deleteCategory(uuid);
        return ResponseEntity.noContent().build();
    }



}
