package com.crusty.blog.controllers;

import com.crusty.blog.domain.dtos.CreateTagsRequest;
import com.crusty.blog.domain.dtos.TagDto;
import com.crusty.blog.domain.entities.Tag;
import com.crusty.blog.mappers.TagMapper;
import com.crusty.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags()
    {
        List<Tag> tags = tagService.getAllTags();
        List<TagDto> responses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequest createTagsRequest)
    {
        List<Tag> createdTags = tagService.createNewTags(createTagsRequest.getNames());
        List<TagDto> resp= createdTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") UUID uuid)
    {
        tagService.deleteTag(uuid);
        return ResponseEntity.noContent().build();
    }
}
