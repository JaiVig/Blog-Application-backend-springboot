package com.crusty.blog.controllers;

import com.crusty.blog.domain.dtos.ArticleCreationReq;
import com.crusty.blog.domain.dtos.ArticleCreationReqDto;
import com.crusty.blog.domain.dtos.ArticleDto;
import com.crusty.blog.domain.dtos.ArticleUpdateReqDto;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.User;
import com.crusty.blog.mappers.ArticleMapper;
import com.crusty.blog.services.ArticleService;
import com.crusty.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final ArticleMapper mapper;

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getPublishedArticles(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false) UUID tagId
            )
    {
        List<Article> articleList = articleService.getAllArticles(categoryId,tagId);
        List<ArticleDto> articleDtos= articleList.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(articleDtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ArticleDto> getarticlesByid(
           @PathVariable UUID id
    )
    {
       Article article = articleService.getArticleById(id);
       ArticleDto articleDto = mapper.toDto(article);
       return ResponseEntity.ok(articleDto);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<ArticleDto>> getUnpublishedArticles(
            @RequestAttribute UUID userId
    )
    {
        User loggedUser = userService.getUserById(userId);
        List<Article> alldrafts= articleService.getAllDrafts(loggedUser);
        List<ArticleDto> articleDtos = alldrafts.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(articleDtos);
    }

    @PostMapping
    public ResponseEntity<ArticleDto> createPost(
            @RequestBody ArticleCreationReqDto creationReqDto,
            @RequestAttribute UUID userId
            )
    {
        User loggedUser = userService.getUserById(userId);
        ArticleCreationReq receivedArticle = mapper.toArticleCreationReq(creationReqDto);
        Article createdArticle= articleService.createArticle(loggedUser,receivedArticle);
        ArticleDto articleDto = mapper.toDto(createdArticle);
        return new ResponseEntity<>(articleDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ArticleDto> updatePost(
            @PathVariable UUID id,
            @RequestBody ArticleUpdateReqDto updateReqDto
    )
    {
        Article article= articleService.updateArticle(id,mapper.toArticleUpdateReq(updateReqDto));
        ArticleDto articleDto = mapper.toDto(article);
        return ResponseEntity.ok(articleDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deltePost(@PathVariable UUID id)
    {
        articleService.DeleteArticle(id);
        return ResponseEntity.noContent().build();
    }

}
