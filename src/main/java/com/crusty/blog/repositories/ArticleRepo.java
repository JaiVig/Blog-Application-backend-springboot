package com.crusty.blog.repositories;

import com.crusty.blog.domain.ArticleStatus;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.Category;
import com.crusty.blog.domain.entities.Tag;
import com.crusty.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepo extends JpaRepository<Article, UUID> {

    List<Article> findAllByStatusAndAllCategoriesAndAllTagsContaining(ArticleStatus status, Category category, Tag tag);
    List<Article> findAllByStatusAndAllCategories(ArticleStatus status, Category category);
    List<Article> findAllByStatusAndAllTagsContaining(ArticleStatus status,Tag tag);
    List<Article> findAllByStatus(ArticleStatus status);
    List<Article> findAllByStatusAndMyAuthor(ArticleStatus status, User author);
}
