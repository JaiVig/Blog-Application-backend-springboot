package com.crusty.blog.repositories;

import com.crusty.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.allArticles")
    List<Category> findAllWithArticleCount();

    boolean existsByNameIgnoreCase(String name);
}
