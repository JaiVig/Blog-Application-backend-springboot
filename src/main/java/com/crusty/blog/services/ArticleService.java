package com.crusty.blog.services;

import com.crusty.blog.domain.ArticleStatus;
import com.crusty.blog.domain.dtos.ArticleCreationReq;
import com.crusty.blog.domain.dtos.ArticleUpdateReq;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface ArticleService {

    Article getArticleById(UUID uuid);

    List<Article> getAllArticles(UUID categoryid,UUID tagid);
    List<Article> getAllDrafts(User user);
    Article createArticle(User user,ArticleCreationReq creationReq);
    Article updateArticle(UUID articleid, ArticleUpdateReq updatedArticle);
    void DeleteArticle(UUID uuid);
}
