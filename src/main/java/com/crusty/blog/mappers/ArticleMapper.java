package com.crusty.blog.mappers;

import com.crusty.blog.domain.dtos.*;
import com.crusty.blog.domain.entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {


    @Mapping(target = "category",source = "allCategories")
    @Mapping(target = "tags",source = "allTags")
    @Mapping(target = "author",source = "myAuthor")
    ArticleDto toDto(Article article);



    ArticleCreationReq toArticleCreationReq(ArticleCreationReqDto creationReqDto);

    ArticleUpdateReq toArticleUpdateReq(ArticleUpdateReqDto updateReqDto);

}
