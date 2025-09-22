package com.crusty.blog.mappers;


import com.crusty.blog.domain.ArticleStatus;
import com.crusty.blog.domain.dtos.CategoryDto;
import com.crusty.blog.domain.dtos.CreateCategoryRequest;
import com.crusty.blog.domain.entities.Article;
import com.crusty.blog.domain.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "allArticles", qualifiedByName = "calcCount")
    CategoryDto toDto(Category category);

    Category fromRequestToEntity(CreateCategoryRequest categoryRequest);

    @Named("calcCount")
    default long calcCount(List<Article> articles){
        if (articles == null) return 0;
        return articles.stream()
                .filter(a -> ArticleStatus.PUBLISHED.equals(a.getStatus())) // adjust enum/name as needed
                .count();
    }
}
