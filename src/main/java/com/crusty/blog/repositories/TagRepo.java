package com.crusty.blog.repositories;

import com.crusty.blog.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepo extends JpaRepository<Tag, UUID> {

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.allRelatedArticles")
    List<Tag> findAllwithpostcount();

    List<Tag> findByNameIn(Set<String> tagNames);
}
