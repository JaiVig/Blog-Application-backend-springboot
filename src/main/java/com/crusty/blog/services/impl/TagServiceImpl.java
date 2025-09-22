package com.crusty.blog.services.impl;

import com.crusty.blog.domain.entities.Tag;
import com.crusty.blog.repositories.TagRepo;
import com.crusty.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    public final TagRepo tagRepo;
    @Override
    public List<Tag> getAllTags() {
        return tagRepo.findAllwithpostcount();
    }

    @Transactional
    @Override
    public List<Tag> createNewTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepo.findByNameIn(tagNames);
        Set<String> existingNames = existingTags.stream().map(t->t.getName()).collect(Collectors.toSet());
        List<Tag> toDB = tagNames.stream().filter(n->!existingNames.contains(n))
                .map(n->Tag.builder().name(n).allRelatedArticles(new HashSet<>()).build())
                .collect(Collectors.toList());

        List<Tag> addedTags = new ArrayList<>();
        if(toDB!=null)
            addedTags = tagRepo.saveAll(toDB);

        addedTags.addAll(existingTags);//if toDB is null, that means all tags user is trying to add already exist, so just returnn that.

        return addedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID uuid) {
       Tag tag = tagRepo.findById(uuid).orElseThrow(() -> new EntityNotFoundException("No Tag exists with id "+ uuid));
       if(!tag.getAllRelatedArticles().isEmpty())
       {
           throw new IllegalStateException("Cannot delete tag "+ tag.getName()+" as it has posts associated with it : "+ tag.getAllRelatedArticles().size());

       }
       tagRepo.deleteById(uuid);
    }

    @Override
    public Tag getTag(UUID uuid) {
       return tagRepo.findById(uuid).orElseThrow(()-> new EntityNotFoundException("Couldnt find tag with id " + uuid));
    }

    @Override
    public List<Tag> getTagsById(Set<UUID> tagids) {
        List<Tag> tags = tagRepo.findAllById(tagids);
        if(tags.size() != tagids.size())
        {
            throw new EntityNotFoundException("Not all specific tags found!");
        }
        return tags;
    }
}
