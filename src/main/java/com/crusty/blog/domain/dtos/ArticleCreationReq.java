package com.crusty.blog.domain.dtos;


import com.crusty.blog.domain.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleCreationReq {
    private String title;
    private String content;
    private UUID categoryId;
    @Builder.Default
    private Set<UUID> tagIds = new HashSet<>();
    private ArticleStatus status;
}
