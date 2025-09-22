package com.crusty.blog.services;

import com.crusty.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    public List<Tag> getAllTags();

    List<Tag> createNewTags(Set<String> tagNames);

    void deleteTag(UUID uuid);

    Tag getTag(UUID uuid);

    List<Tag> getTagsById(Set<UUID> tagids);
}
