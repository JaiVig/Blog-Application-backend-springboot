package com.crusty.blog.services.impl;

import com.crusty.blog.domain.ArticleStatus;
import com.crusty.blog.domain.dtos.ArticleCreationReq;
import com.crusty.blog.domain.dtos.ArticleUpdateReq;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.Category;
import com.crusty.blog.domain.entities.Tag;
import com.crusty.blog.domain.entities.User;
import com.crusty.blog.repositories.ArticleRepo;
import com.crusty.blog.repositories.UserRepo;
import com.crusty.blog.services.ArticleService;
import com.crusty.blog.services.CategoryService;
import com.crusty.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepo articleRepo;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final UserRepo userRepo;

    @Override
    public Article getArticleById(UUID uuid) {
        return articleRepo.findById(uuid).orElseThrow(() -> new EntityNotFoundException("No article exists with id "+ uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getAllArticles(UUID categoryid, UUID tagid) {
        if(categoryid!=null && tagid != null)
        {
            Category category = categoryService.getCategory(categoryid);
            Tag tag = tagService.getTag(tagid);
            return articleRepo.findAllByStatusAndAllCategoriesAndAllTagsContaining(ArticleStatus.PUBLISHED,category,tag);
        }

        if(categoryid!=null )
        {
            Category category = categoryService.getCategory(categoryid);

            return articleRepo.findAllByStatusAndAllCategories(ArticleStatus.PUBLISHED,category);
        }

        if(tagid != null)
        {

            Tag tag = tagService.getTag(tagid);
            return articleRepo.findAllByStatusAndAllTagsContaining(ArticleStatus.PUBLISHED,tag);
        }

        return articleRepo.findAllByStatus(ArticleStatus.PUBLISHED);
    }

    @Override
    public List<Article> getAllDrafts(User user) {
        return articleRepo.findAllByStatusAndMyAuthor(ArticleStatus.DRAFT,user);
    }

    @Override
    @Transactional
    public Article createArticle(User user, ArticleCreationReq creationReq) {
        Article newArticle = new Article();
        newArticle.setTitle(creationReq.getTitle());
        newArticle.setStatus(creationReq.getStatus());
        newArticle.setContent(creationReq.getContent());
        newArticle.setMyAuthor(user);

        Category category = categoryService.getCategory(creationReq.getCategoryId());
        newArticle.setAllCategories(category);

        newArticle.setReadingTime(calculateReadingTime(creationReq.getContent()));

        List<Tag> tags= tagService.getTagsById(creationReq.getTagIds());
        newArticle.setAllTags(new HashSet<>(tags));
        return articleRepo.save(newArticle);
    }

    @Override
    public Article updateArticle(UUID articleid, ArticleUpdateReq updatedArticle) {
        Article existingArticle = articleRepo.findById(articleid).orElseThrow(() -> new EntityNotFoundException("Article not found having the id  "+ articleid));

        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        existingArticle.setReadingTime(calculateReadingTime(updatedArticle.getContent()));
        existingArticle.setStatus(updatedArticle.getStatus());
        if(existingArticle.getAllCategories().getId() != updatedArticle.getCategoryId())
        {
            Category newCategory = categoryService.getCategory(updatedArticle.getCategoryId());
            existingArticle.setAllCategories(newCategory);
        }

        Set<UUID> existingtags = existingArticle.getAllTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if(!existingtags.equals(updatedArticle.getTagIds()))
        {
            List<Tag> tags= tagService.getTagsById(updatedArticle.getTagIds());
            existingArticle.setAllTags(new HashSet<>(tags));
        }
        return articleRepo.save(existingArticle);
    }

    @Override
    @Transactional
    public void DeleteArticle(UUID uuid) {
        Article existingArticle = articleRepo.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Article not found having the id  "+ uuid));
        articleRepo.delete(existingArticle);
    }


    private Integer calculateReadingTime(String content) {
        if(content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / 240);
    }

}
