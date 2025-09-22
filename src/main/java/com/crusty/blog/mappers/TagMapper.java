package com.crusty.blog.mappers;

import com.crusty.blog.domain.ArticleStatus;
import com.crusty.blog.domain.dtos.TagDto;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "allRelatedArticles", qualifiedByName = "calcArticleCount")
    TagDto toTagResponse(Tag tag);

    @Named("calcArticleCount")
    default Integer calcArticleCount(Set<Article> allRelatedArticles)
    {
        if(allRelatedArticles == null) return 0;

        return (int) allRelatedArticles.stream()
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .count();
    }

}
